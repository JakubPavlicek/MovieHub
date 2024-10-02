package com.moviehub.service;

import com.moviehub.dto.MovieFilter;
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

    public Page<Movie> getMovies(Integer page, Integer limit, String sort, MovieFilter filter) {
        Pageable pageable = PageRequest.of(page, limit, parseService.parseMovieSort(sort));

        Specification<Movie> specification = Specification.where(parseService.parseName(filter.getName()))
                                                          .and(parseService.parseReleaseDate(filter.getReleaseDate()))
                                                          .and(parseService.parseDuration(filter.getDuration()))
                                                          .and(parseService.parseDescription(filter.getDescription()))
                                                          .and(parseService.parseRating(filter.getRating()))
                                                          .and(parseService.parseReviewCount(filter.getReviewCount()))
                                                          .and(parseService.parseDirector(filter.getDirector()))
                                                          .and(parseService.parseActors(filter.getActors()))
                                                          .and(parseService.parseGenres(filter.getGenres()))
                                                          .and(parseService.parseCountries(filter.getCountries()))
                                                          .and(MovieSpecification.searchByKeyword(filter.getKeyword()));

        return movieRepository.findAll(specification, pageable);
    }

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

}
