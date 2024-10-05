package com.moviehub.service;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.Movie_;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.repository.MovieRepository;
import com.moviehub.specification.MovieSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieSearchService {

    private final MovieRepository movieRepository;
    private final ParseService parseService;

    private static final Sort SORT_BY_UPDATED_AT_DESC = Sort.by(Sort.Direction.DESC, Movie_.UPDATED_AT);

    public Page<Movie> getMoviesWithGenre(Genre genre, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        return movieRepository.findAllByGenresContaining(genre, pageable);
    }

    public Page<Movie> getMoviesWithCountry(Country country, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        return movieRepository.findAllByCountriesContaining(country, pageable);
    }

    public Page<Movie> getMoviesWithProductionCompany(ProductionCompany productionCompany, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        return movieRepository.findAllByProductionContaining(productionCompany, pageable);
    }

    public Page<Movie> getMoviesWithDirector(Director director, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        return movieRepository.findAllByDirector(director, pageable);
    }

    public Page<Movie> getMoviesWithActor(Actor actor, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, SORT_BY_UPDATED_AT_DESC);

        return movieRepository.findAllByActorsContaining(actor, pageable);
    }

    public Page<Movie> listMovies(Integer page, Integer limit, String sort) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));

        return movieRepository.findAll(pageable);
    }

    public Page<Movie> filterMovies(Integer page, Integer limit, String sort, String releaseYear, String genre, String country) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));

        Specification<Movie> specification = Specification.where(parseService.parseReleaseYear(releaseYear))
                                                          .and(parseService.parseGenre(genre))
                                                          .and(parseService.parseCountry(country));

        return movieRepository.findAll(specification, pageable);
    }

    public Page<Movie> searchMovies(Integer page, Integer limit, String sort, String keyword) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));
        Specification<Movie> specification = MovieSpecification.searchByKeyword(keyword);

        return movieRepository.findAll(specification, pageable);
    }

}
