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

import java.time.LocalDate;

public class MovieSpecification {

    private MovieSpecification() {
    }

    public static Specification<Movie> nameEqualTo(String name) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.NAME)),
            name.toLowerCase()
        );
    }

    public static Specification<Movie> nameContains(String name) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get(Movie_.NAME)),
            "%" + name.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> releaseDateEqualTo(LocalDate releaseDate) {
        return (root, query, cb) -> cb.equal(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateNotEqualTo(LocalDate releaseDate) {
        return (root, query, cb) -> cb.notEqual(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateBefore(LocalDate releaseDate) {
        return (root, query, cb) -> cb.lessThan(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateAfter(LocalDate releaseDate) {
        return (root, query, cb) -> cb.greaterThan(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateBeforeOrEqualTo(LocalDate releaseDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateAfterOrEqualTo(LocalDate releaseDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> durationEqualTo(Integer duration) {
        return (root, query, cb) -> cb.equal(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationNotEqualTo(Integer duration) {
        return (root, query, cb) -> cb.notEqual(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationLessThan(Integer duration) {
        return (root, query, cb) -> cb.lessThan(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationGreaterThan(Integer duration) {
        return (root, query, cb) -> cb.greaterThan(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationLessThanOrEqualTo(Integer duration) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationGreaterThanOrEqualTo(Integer duration) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> descriptionEqualTo(String description) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.DESCRIPTION)),
            description.toLowerCase()
        );
    }

    public static Specification<Movie> descriptionContains(String description) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get(Movie_.DESCRIPTION)),
            "%" + description.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> ratingEqualTo(Double rating) {
        return (root, query, cb) -> cb.equal(root.get(Movie_.RATING), rating);
    }

    public static Specification<Movie> ratingLessThan(Double rating) {
        return (root, query, cb) -> cb.lessThan(root.get(Movie_.RATING), rating);
    }

    public static Specification<Movie> ratingGreaterThan(Double rating) {
        return (root, query, cb) -> cb.greaterThan(root.get(Movie_.RATING), rating);
    }

    public static Specification<Movie> ratingLessThanOrEqualTo(Double rating) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Movie_.RATING), rating);
    }

    public static Specification<Movie> ratingGreaterThanOrEqualTo(Double rating) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Movie_.RATING), rating);
    }

    public static Specification<Movie> reviewCountEqualTo(Integer reviewCount) {
        return (root, query, cb) -> cb.equal(root.get(Movie_.REVIEW_COUNT), reviewCount);
    }

    public static Specification<Movie> reviewCountLessThan(Integer reviewCount) {
        return (root, query, cb) -> cb.lessThan(root.get(Movie_.REVIEW_COUNT), reviewCount);
    }

    public static Specification<Movie> reviewCountGreaterThan(Integer reviewCount) {
        return (root, query, cb) -> cb.greaterThan(root.get(Movie_.REVIEW_COUNT), reviewCount);
    }

    public static Specification<Movie> reviewCountLessThanOrEqualTo(Integer reviewCount) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Movie_.REVIEW_COUNT), reviewCount);
    }

    public static Specification<Movie> reviewCountGreaterThanOrEqualTo(Integer reviewCount) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Movie_.REVIEW_COUNT), reviewCount);
    }

    public static Specification<Movie> directorEqualTo(String director) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.DIRECTOR).get(Director_.NAME)),
            director.toLowerCase()
        );
    }

    public static Specification<Movie> directorContains(String director) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get(Movie_.DIRECTOR).get(Director_.NAME)),
            "%" + director.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> actorEqualTo(String actor) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.CAST).get(MovieCast_.ACTOR).get(Actor_.NAME)),
            actor.toLowerCase()
        );
    }

    public static Specification<Movie> actorContains(String actor) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get(Movie_.CAST).get(MovieCast_.ACTOR).get(Actor_.NAME)),
            "%" + actor.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> genreEqualTo(String genre) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.GENRES).get(Genre_.NAME)),
            genre.toLowerCase()
        );
    }

    public static Specification<Movie> genreContains(String genre) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get(Movie_.GENRES).get(Genre_.NAME)),
            "%" + genre.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> countryEqualTo(String country) {
        return (root, query, cb) -> cb.equal(
            cb.lower(root.get(Movie_.COUNTRIES).get(Country_.NAME)),
            country.toLowerCase()
        );
    }

    public static Specification<Movie> countryContains(String country) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get(Movie_.COUNTRIES).get(Country_.NAME)),
            "%" + country.toLowerCase() + "%"
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
