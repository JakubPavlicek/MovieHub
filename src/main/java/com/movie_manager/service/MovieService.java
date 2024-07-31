package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.mapper.entity.MovieEntityMapper;
import com.movie_manager.repository.MovieRepository;
import com.movie_manager.specification.Criteria;
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

    public Page<Movie> getMovies(Integer page, Integer limit, String sort, String name, String release, String length, String description, String director, List<String> actor, List<String> genre, List<String> country) {
        Pageable pageable = PageRequest.of(page, limit, parseSort(sort));

        Specification<Movie> specification = Specification.where(parseName(name))
                                                          .and(parseRelease(release));

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

    private Specification<Movie> parseName(String name) {
        return parseFilter(name, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.hasName(value);
            case "like" -> MovieSpecification.likeName(value);
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria + " in filter: " + name);
        });
    }

    private Specification<Movie> parseRelease(String release) {
        return parseFilter(release, (criteria, value) -> switch (criteria) {
            case "eq" -> MovieSpecification.hasRelease(value);
            case "lt" -> MovieSpecification.beforeRelease(value);
            case "gt" -> MovieSpecification.afterRelease(value);
            default -> throw new IllegalArgumentException("Invalid criteria: " + criteria + " in filter: " + release);
        });
    }

}
