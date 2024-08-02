package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.exception.FilterException;
import com.movie_manager.exception.MovieNotFoundException;
import com.movie_manager.exception.ParseException;
import com.movie_manager.exception.SortException;
import com.movie_manager.mapper.entity.MovieFieldMapper;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MovieService {

    private static final String SORT_REGEX = "(name|releaseDate|length|description|director).(asc|desc)(?:,|$)";

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
                              .orElseThrow(() -> new MovieNotFoundException("Movie with ID: " + movieId + " not found"));
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
        if (movie.getReleaseDate() != null)
            existingMovie.setReleaseDate(movie.getReleaseDate());
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

    @Transactional
    public Page<Movie> getMovies(Integer page, Integer limit, String sort, String name, String releaseDate, String length, String description, String director, List<String> actors, List<String> genres, List<String> countries) {
        Pageable pageable = PageRequest.of(page, limit, parseSort(sort));

        Specification<Movie> specification = Specification.where(parseName(name))
                                                          .and(parseReleaseDate(releaseDate))
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
            String field = MovieFieldMapper.mapToMovieField(matcher.group(1));
            Sort.Direction direction = Sort.Direction.fromString(matcher.group(2));
            orders.add(new Sort.Order(direction, field));
        }

        if (orders.isEmpty()) {
            throw new SortException("Invalid sort value: " + sort);
        }

        return Sort.by(orders);
    }

    private Specification<Movie> parseFilter(String filter, Map<String, Function<String, Specification<Movie>>> criteriaHandlers) {
        String[] parts = filter.split(":", 2);

        if (parts.length != 2) {
            throw new FilterException("Invalid filter format: " + filter);
        }

        String criteria = parts[0];
        String value = parts[1];

        Function<String, Specification<Movie>> handler = criteriaHandlers.get(criteria);

        if (handler == null) {
            throw new FilterException("Invalid criteria '" + criteria + "' in filter: " + filter);
        }

        return handler.apply(value);
    }

    private Specification<Movie> parseFilterList(List<String> filters, Map<String, Function<String, Specification<Movie>>> criteriaHandlers) {
        // initialize with a valid specification
        Specification<Movie> specification = MovieSpecification.conjunction();

        for (String filter : filters) {
            Specification<Movie> filterSpec = parseFilter(filter, criteriaHandlers);
            specification = specification.and(filterSpec);
        }

        return specification;
    }

    private Specification<Movie> parseName(String name) {
        return parseFilter(name, Map.of(
            "eq",   MovieSpecification::nameEqualTo,
            "like", MovieSpecification::nameContains
        ));
    }

    private Specification<Movie> parseReleaseDate(String release) {
        return parseFilter(release, Map.of(
            "eq",  value -> MovieSpecification.releaseDateEqualTo(parseDate(value)),
            "neq", value -> MovieSpecification.releaseDateNotEqualTo(parseDate(value)),
            "lt",  value -> MovieSpecification.releaseDateBefore(parseDate(value)),
            "gt",  value -> MovieSpecification.releaseDateAfter(parseDate(value)),
            "lte", value -> MovieSpecification.releaseDateBeforeOrEqualTo(parseDate(value)),
            "gte", value -> MovieSpecification.releaseDateAfterOrEqualTo(parseDate(value))
        ));
    }

    private Specification<Movie> parseLength(String length) {
        return parseFilter(length, Map.of(
            "eq",  value -> MovieSpecification.lengthEqualTo(parseInteger(value)),
            "neq", value -> MovieSpecification.lengthNotEqualTo(parseInteger(value)),
            "lt",  value -> MovieSpecification.lengthLessThan(parseInteger(value)),
            "gt",  value -> MovieSpecification.lengthGreaterThan(parseInteger(value)),
            "lte", value -> MovieSpecification.lengthLessThanOrEqualTo(parseInteger(value)),
            "gte", value -> MovieSpecification.lengthGreaterThanOrEqualTo(parseInteger(value))
        ));
    }

    private Specification<Movie> parseDescription(String description) {
        return parseFilter(description, Map.of(
            "eq",   MovieSpecification::descriptionEqualTo,
            "like", MovieSpecification::descriptionContains
        ));
    }

    private Specification<Movie> parseDirector(String director) {
        return parseFilter(director, Map.of(
            "eq",   MovieSpecification::directorEqualTo,
            "like", MovieSpecification::directorContains
        ));
    }

    private Specification<Movie> parseActors(List<String> actors) {
        return parseFilterList(actors, Map.of(
            "eq",   MovieSpecification::actorEqualTo,
            "like", MovieSpecification::actorContains
        ));
    }

    private Specification<Movie> parseGenres(List<String> genres) {
        return parseFilterList(genres, Map.of(
            "eq", MovieSpecification::genreEqualTo
        ));
    }

    private Specification<Movie> parseCountries(List<String> countries) {
        return parseFilterList(countries, Map.of(
            "eq", MovieSpecification::countryEqualTo
        ));
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new ParseException("Invalid date format: " + date);
        }
    }

    private Integer parseInteger(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException exception) {
            throw new ParseException("Invalid integer format: " + integer);
        }
    }

}
