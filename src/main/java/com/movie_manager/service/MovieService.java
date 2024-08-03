package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.exception.MovieNotFoundException;
import com.movie_manager.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    private final DirectorService directorService;
    private final ActorService actorService;
    private final CountryService countryService;
    private final GenreService genreService;

    private final ParseService parseService;

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
        return movieRepository.findById(movieId)
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
        Pageable pageable = PageRequest.of(page, limit, parseService.parseSort(sort));

        Specification<Movie> specification = Specification.where(parseService.parseName(name))
                                                          .and(parseService.parseReleaseDate(releaseDate))
                                                          .and(parseService.parseLength(length))
                                                          .and(parseService.parseDescription(description))
                                                          .and(parseService.parseDirector(director))
                                                          .and(parseService.parseActors(actors))
                                                          .and(parseService.parseGenres(genres))
                                                          .and(parseService.parseCountries(countries));

        return movieRepository.findAll(specification, pageable);
    }

}
