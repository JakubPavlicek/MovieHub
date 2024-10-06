package com.moviehub.specification;

import com.moviehub.entity.Actor_;
import com.moviehub.entity.Country_;
import com.moviehub.entity.Director_;
import com.moviehub.entity.Genre_;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast_;
import com.moviehub.entity.Movie_;
import com.moviehub.entity.ProductionCompany_;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

    private MovieSpecification() {
    }

    public static Specification<Movie> genreEqualTo(String genre) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.GENRES).get(Genre_.NAME)),
            genre.toLowerCase()
        );
    }

    public static Specification<Movie> countryEqualTo(String country) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.COUNTRIES).get(Country_.NAME)),
            country.toLowerCase()
        );
    }

    public static Specification<Movie> releaseYearEqualTo(String year) {
        return (root, query, cb) -> cb.equal(
            cb.function("YEAR", String.class, root.get(Movie_.RELEASE_DATE)),
            year
        );
    }

    public static Specification<Movie> searchByKeyword(String keyword) {
        return (root, query, cb) -> {
            query.distinct(true);

            return cb.or(
                cb.like(
                    cb.lower(root.get(Movie_.NAME)),
                    "%" + keyword.toLowerCase() + "%"
                ),
                cb.like(
                    cb.lower(root.get(Movie_.DIRECTOR).get(Director_.NAME)),
                    "%" + keyword.toLowerCase() + "%"
                ),
                cb.like(
                    cb.lower(root.get(Movie_.CAST).get(MovieCast_.ACTOR).get(Actor_.NAME)),
                    "%" + keyword.toLowerCase() + "%"
                ),
                cb.like(
                    cb.lower(root.get(Movie_.PRODUCTION).get(ProductionCompany_.NAME)),
                    "%" + keyword.toLowerCase() + "%"
                )
            );
        };
    }

    public static Specification<Movie> conjunction() {
        return (root, query, cb) -> cb.conjunction();
    }

}
