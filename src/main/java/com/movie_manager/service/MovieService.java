package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.mapper.entity.MovieEntityMapper;
import com.movie_manager.repository.MovieRepository;
import com.movie_manager.specification.MovieSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MovieService {

    private static final String SORT_REGEX = "(name|release|length|description|director).(asc|desc)(?:,|$)";

    private final MovieRepository movieRepository;

    private final DirectorService directorService;
    private final ActorService actorService;
    private final CountryService countryService;
    private final GenreService genreService;

    @Transactional
    public Movie addMovie(Movie movie) {
        Director existingDirector = directorService.getExistingDirector(movie.getDirector());
        movie.setDirector(existingDirector);

        List<Actor> existingActors = actorService.getExistingActors(movie.getActors());
        movie.setActors(existingActors);

        List<Country> existingCountries = countryService.getExistingCountries(movie.getCountries());
        movie.setCountries(existingCountries);

        List<Genre> existingGenres = genreService.getExistingGenres(movie.getGenres());
        movie.setGenres(existingGenres);

        return movieRepository.save(movie);
    }

    @Transactional
    public Movie getMovie(String movieId) {
        return movieRepository.findByMovieId(movieId)
                              .orElseThrow(() -> new RuntimeException("Movie with ID: " + movieId + " not found"));
    }

    @Transactional
    public void deleteMovie(String movieId) {
        Movie movie = getMovie(movieId);
        movieRepository.delete(movie);
    }

    @Transactional
    public Movie updateMovie(String movieId, Movie movie) {
        Movie existingMovie = getMovie(movieId);

        updateMovieFields(movie, existingMovie);
        updateMovieRelatedEntities(movie, existingMovie);

        return movieRepository.save(existingMovie);
    }

    private static void updateMovieFields(Movie movie, Movie existingMovie) {
        if (movie.getName() != null)
            existingMovie.setName(movie.getName());
        if (movie.getRelease() != null)
            existingMovie.setRelease(movie.getRelease());
        if (movie.getLength() != null)
            existingMovie.setLength(movie.getLength());
        if (movie.getDescription() != null)
            existingMovie.setDescription(movie.getDescription());
    }

    private void updateMovieRelatedEntities(Movie movie, Movie existingMovie) {
        if (movie.getDirector() != null) {
            Director existingDirector = directorService.getExistingDirector(movie.getDirector());
            existingMovie.setDirector(existingDirector);
        }
        if (movie.getActors() != null && !movie.getActors().isEmpty()) {
            List<Actor> existingActors = actorService.getExistingActors(movie.getActors());
            existingMovie.setActors(existingActors);
        }
        if (movie.getCountries() != null && !movie.getCountries().isEmpty()) {
            List<Country> existingCountries = countryService.getExistingCountries(movie.getCountries());
            existingMovie.setCountries(existingCountries);
        }
        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            List<Genre> existingGenres = genreService.getExistingGenres(movie.getGenres());
            existingMovie.setGenres(existingGenres);
        }
    }

    public Page<Movie> getMovies(Integer page, Integer limit, String sort, String name, String release, String length, String description, String director, List<String> actors, List<String> genres, List<String> countries) {
        Pageable pageable = PageRequest.of(page, limit, parseSort(sort));

        Specification<Movie> specification = Specification.where(parseName(name))
                                                          .and(parseRelease(release))
                                                          .and(parseLength(length))
                                                          .and(parseDescription(description))
                                                          .and(parseDirector(director))
                                                          .and(parseActors(actors))
                                                          .and(parseGenres(genres))
                                                          .and(parseCountries(countries));

        return movieRepository.findAll(specification, pageable);
    }

    private Sort parseSort(String sort) {
        Pattern pattern = Pattern.compile(SORT_REGEX);
        Matcher matcher = pattern.matcher(sort);

        List<Sort.Order> orders = new ArrayList<>();

        while (matcher.find()) {
            String field = MovieEntityMapper.mapToMovieField(matcher.group(1));
            Sort.Direction direction = Sort.Direction.fromString(matcher.group(2));
            orders.add(new Sort.Order(direction, field));
        }

        if (orders.isEmpty()) {
            throw new RuntimeException("Invalid sort value: " + sort);
        }

        return Sort.by(orders);
    }

    private Specification<Movie> parseFilter(String filter, BiFunction<String, String, Specification<Movie>> criteriaHandler) {
        String[] parts = filter.split(":", 2);

        if (parts.length != 2) {
            throw new RuntimeException("Invalid filter format: " + filter);
        }

        String criteria = parts[0];
        String value = parts[1];

        return criteriaHandler.apply(criteria, value);
    }

    private Specification<Movie> parseFilterList(List<String> filters, BiFunction<String, String, Specification<Movie>> criteriaHandler) {
        // initialize with a valid specification
        Specification<Movie> specification = MovieSpecification.conjunction();

        for (String filter : filters) {
            Specification<Movie> filterSpec = parseFilter(filter, criteriaHandler);
            specification = specification.and(filterSpec);
        }

        return specification;
    }

    private Specification<Movie> handleInvalidCriteria(String criteria, String filter) {
        throw new IllegalArgumentException("Invalid criteria: " + criteria + " in filter: " + filter);
    }

    private Specification<Movie> parseName(String name) {
        return parseFilter(name, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.nameEqualTo(value);
            case "like" -> MovieSpecification.nameContains(value);
            default -> handleInvalidCriteria(criteria, name);
        });
    }

    private Specification<Movie> parseRelease(String release) {
        return parseFilter(release, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.releaseEqualTo(value);
            case "lt" -> MovieSpecification.releaseBefore(value);
            case "gt" -> MovieSpecification.releaseAfter(value);
            default -> handleInvalidCriteria(criteria, release);
        });
    }

    private Specification<Movie> parseLength(String length) {
        return parseFilter(length, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.lengthEqualTo(value);
            case "neq" -> MovieSpecification.lengthNotEqualTo(value);
            case "lt" -> MovieSpecification.lengthLessThan(value);
            case "gr" -> MovieSpecification.lengthGreaterThan(value);
            case "lte" -> MovieSpecification.lengthLessThanOrEqualTo(value);
            case "gte" -> MovieSpecification.lengthGreaterThanOrEqualTo(value);
            default -> handleInvalidCriteria(criteria, length);
        });
    }

    private Specification<Movie> parseDescription(String description) {
        return parseFilter(description, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.descriptionEqualTo(value);
            case "like" -> MovieSpecification.descriptionContains(value);
            default -> handleInvalidCriteria(criteria, description);
        });
    }

    private Specification<Movie> parseDirector(String director) {
        return parseFilter(director, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.directorEqualTo(value);
            case "like" -> MovieSpecification.directorContains(value);
            default -> handleInvalidCriteria(criteria, director);
        });
    }

    private Specification<Movie> parseActors(List<String> actors) {
        return parseFilterList(actors, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.actorEqualTo(value);
            case "like" -> MovieSpecification.actorContains(value);
            default -> handleInvalidCriteria(criteria, criteria + ":" + value);
        });
    }

    private Specification<Movie> parseGenres(List<String> genres) {
        return parseFilterList(genres, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.genreEqualTo(value);
            case "like" -> MovieSpecification.genreContains(value);
            default -> handleInvalidCriteria(criteria, criteria + ":" + value);
        });
    }

    private Specification<Movie> parseCountries(List<String> countries) {
        return parseFilterList(countries, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.countryEqualTo(value);
            case "like" -> MovieSpecification.countryContains(value);
            default -> handleInvalidCriteria(criteria, criteria + ":" + value);
        });
    }

}
