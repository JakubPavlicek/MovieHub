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

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MovieSearchService {

    private final MovieRepository movieRepository;
    private final ParseService parseService;

    private static final Sort SORT_BY_UPDATED_AT_DESC = Sort.by(Sort.Direction.DESC, Movie_.UPDATED_AT);

    public Page<Movie> getMoviesWithGenre(UUID genreId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByGenreId(genreId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    public Page<Movie> getMoviesWithCountry(UUID countryId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByCountryId(countryId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    public Page<Movie> getMoviesWithProductionCompany(UUID companyId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByCompanyId(companyId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    public Page<Movie> getMoviesWithDirector(UUID directorId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByDirectorId(directorId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    public Page<Movie> getMoviesWithActor(UUID actorId, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        Page<UUID> movieIds = movieRepository.findMovieIdsByActorId(actorId, pageable);
        List<Movie> moviesWithGenresByIds = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(moviesWithGenresByIds, pageable, movieIds.getTotalElements());
    }

    public Page<Movie> getMovies(Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));

        Page<UUID> movieIds = movieRepository.findAllMovieIds(pageable);
        List<Movie> movies = movieRepository.findMoviesWithGenresByIds(movieIds.getContent());

        return new PageImpl<>(movies, pageable, movieIds.getTotalElements());
    }

    public Page<Movie> filterMovies(Integer page, Integer limit, String sort, String releaseYear, String genre, String country) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));

        Specification<Movie> specification = Specification.where(parseService.parseReleaseYear(releaseYear))
                                                          .and(parseService.parseGenre(genre))
                                                          .and(parseService.parseCountry(country));

        return getMoviePage(specification, pageable);
    }

    public Page<Movie> searchMovies(Integer page, Integer limit, String sort, String keyword) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));
        Specification<Movie> specification = MovieSpecification.searchByKeyword(keyword);

        return getMoviePage(specification, pageable);
    }

    private PageImpl<Movie> getMoviePage(Specification<Movie> specification, Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(specification, pageable);
        List<UUID> movieIds = movies.getContent().stream().map(Movie::getId).toList();
        List<Movie> movieList = movieRepository.findMoviesWithGenresByIds(movieIds);

        return new PageImpl<>(movieList, pageable, movies.getTotalElements());
    }

}
