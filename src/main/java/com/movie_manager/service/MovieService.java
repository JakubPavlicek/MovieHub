package com.movie_manager.service;

import com.movie_manager.entity.Actor;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Director;
import com.movie_manager.entity.Genre;
import com.movie_manager.entity.Movie;
import com.movie_manager.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

}
