package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.entity.Movie_;
import com.moviehub.repository.MovieRepository;
import com.moviehub.specification.MovieSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for searching and retrieving movies based on various criteria.
/// This class provides methods to filter, search, and paginate movie results.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MovieSearchService {

    /// Repository for accessing movie data in the database.
    private final MovieRepository movieRepository;

    /// Service for parsing and handling sort options.
    private final ParseService parseService;

    /// Default sorting by the updated_at field in descending order.
    private static final Sort SORT_BY_UPDATED_AT_DESC = Sort.by(Sort.Direction.DESC, Movie_.UPDATED_AT);

    /// Retrieves a paginated list of movies associated with a specific genre.
    ///
    /// @param genreId The UUID of the genre to filter movies by.
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @return A Page containing the list of movies associated with the specified genre.
    public Page<Movie> getMoviesWithGenre(UUID genreId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByGenreId(genreId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    /// Retrieves a paginated list of movies associated with a specific country.
    ///
    /// @param countryId The UUID of the country to filter movies by.
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @return A Page containing the list of movies associated with the specified country.
    public Page<Movie> getMoviesWithCountry(UUID countryId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByCountryId(countryId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    /// Retrieves a paginated list of movies associated with a specific production company.
    ///
    /// @param companyId The UUID of the production company to filter movies by.
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @return A Page containing the list of movies associated with the specified production company.
    public Page<Movie> getMoviesWithProductionCompany(UUID companyId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByCompanyId(companyId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    /// Retrieves a paginated list of movies associated with a specific director.
    ///
    /// @param directorId The UUID of the director to filter movies by.
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @return A Page containing the list of movies associated with the specified director.
    public Page<Movie> getMoviesWithDirector(UUID directorId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByDirectorId(directorId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    /// Retrieves a paginated list of movies associated with a specific actor.
    ///
    /// @param actorId The UUID of the actor to filter movies by.
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @return A Page containing the list of movies associated with the specified actor.
    public Page<Movie> getMoviesWithActor(UUID actorId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByActorId(actorId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    /// Retrieves a paginated list of all movies with optional sorting.
    ///
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @param sort The sorting criteria as a string.
    /// @return A Page containing the list of all movies sorted according to the specified criteria.
    public Page<Movie> getMovies(Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));

        Page<UUID> movieIds = movieRepository.findAllMovieIds(pageable);
        List<Movie> movies = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(movies, pageable, movieIds.getTotalElements());
    }

    /// Filters movies based on specified release year, genre, and country with pagination.
    ///
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @param sort The sorting criteria as a string.
    /// @param releaseYear The release year filter.
    /// @param genre The genre filter.
    /// @param country The country filter.
    /// @return A Page containing the filtered list of movies.
    public Page<Movie> filterMovies(Integer page, Integer limit, String sort, String releaseYear, String genre, String country) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));

        Specification<Movie> specification = Specification.where(parseService.parseReleaseYear(releaseYear))
                                                          .and(parseService.parseGenre(genre))
                                                          .and(parseService.parseCountry(country));

        return getMoviePage(specification, pageable);
    }

    /// Searches for movies based on a keyword with pagination.
    ///
    /// @param page The page number to retrieve.
    /// @param limit The number of movies per page.
    /// @param sort The sorting criteria as a string.
    /// @param keyword The keyword to search for in movie titles or descriptions.
    /// @return A Page containing the list of movies that match the search criteria.
    public Page<Movie> searchMovies(Integer page, Integer limit, String sort, String keyword) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));
        Specification<Movie> specification = MovieSpecification.searchByKeyword(keyword);

        return getMoviePage(specification, pageable);
    }

    /// Retrieves a paginated list of movies based on a given specification.
    ///
    /// @param specification The specification used for querying movies.
    /// @param pageable The pagination information.
    /// @return A PageImpl containing the list of movies that match the specification.
    private PageImpl<Movie> getMoviePage(Specification<Movie> specification, Pageable pageable) {
        log.debug("retrieving movie page based on the ids");

        Page<Movie> movies = movieRepository.findAll(specification, pageable);
        List<UUID> movieIds = movies.getContent()
                                    .stream()
                                    .map(Movie::getId)
                                    .toList();
        List<Movie> movieList = movieRepository.findMoviesWithGenresByIds(movieIds);

        log.debug("movie page retrieved successfully by ids");

        return new PageImpl<>(movieList, pageable, movies.getTotalElements());
    }

}
