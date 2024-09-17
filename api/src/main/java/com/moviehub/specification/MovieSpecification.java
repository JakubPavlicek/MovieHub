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
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            criteriaBuilder.lower(root.get(Movie_.NAME)),
            name.toLowerCase()
        );
    }

    public static Specification<Movie> nameContains(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            criteriaBuilder.lower(root.get(Movie_.NAME)),
            "%" + name.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> releaseDateEqualTo(LocalDate releaseDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateNotEqualTo(LocalDate releaseDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateBefore(LocalDate releaseDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateAfter(LocalDate releaseDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateBeforeOrEqualTo(LocalDate releaseDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> releaseDateAfterOrEqualTo(LocalDate releaseDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Movie_.RELEASE_DATE), releaseDate);
    }

    public static Specification<Movie> durationEqualTo(Integer duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationNotEqualTo(Integer duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationLessThan(Integer duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationGreaterThan(Integer duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationLessThanOrEqualTo(Integer duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> durationGreaterThanOrEqualTo(Integer duration) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Movie_.DURATION), duration);
    }

    public static Specification<Movie> descriptionEqualTo(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            criteriaBuilder.lower(root.get(Movie_.DESCRIPTION)),
            description.toLowerCase()
        );
    }

    public static Specification<Movie> descriptionContains(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            criteriaBuilder.lower(root.get(Movie_.DESCRIPTION)),
            "%" + description.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> directorEqualTo(String director) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            criteriaBuilder.lower(root.get(Movie_.DIRECTOR).get(Director_.NAME)),
            director.toLowerCase()
        );
    }

    public static Specification<Movie> directorContains(String director) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            criteriaBuilder.lower(root.get(Movie_.DIRECTOR).get(Director_.NAME)),
            "%" + director.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> actorEqualTo(String actor) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            criteriaBuilder.lower(root.get(Movie_.CAST).get(MovieCast_.ACTOR).get(Actor_.NAME)),
            actor.toLowerCase()
        );
    }

    public static Specification<Movie> actorContains(String actor) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            criteriaBuilder.lower(root.get(Movie_.CAST).get(MovieCast_.ACTOR).get(Actor_.NAME)),
            "%" + actor.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> genreEqualTo(String genre) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            criteriaBuilder.lower(root.get(Movie_.GENRES).get(Genre_.NAME)),
            genre.toLowerCase()
        );
    }

    public static Specification<Movie> genreContains(String genre) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            criteriaBuilder.lower(root.get(Movie_.GENRES).get(Genre_.NAME)),
            "%" + genre.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> countryEqualTo(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
            criteriaBuilder.lower(root.get(Movie_.COUNTRIES).get(Country_.NAME)),
            country.toLowerCase()
        );
    }

    public static Specification<Movie> countryContains(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            criteriaBuilder.lower(root.get(Movie_.COUNTRIES).get(Country_.NAME)),
            "%" + country.toLowerCase() + "%"
        );
    }

    public static Specification<Movie> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            return criteriaBuilder.or(
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Movie_.NAME)),
                    "%" + keyword + "%"
                ),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Movie_.DIRECTOR).get(Director_.NAME)),
                    "%" + keyword + "%"
                ),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Movie_.CAST).get(MovieCast_.ACTOR).get(Actor_.NAME)),
                    "%" + keyword + "%"
                ),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Movie_.PRODUCTION).get(ProductionCompany_.NAME)),
                    "%" + keyword + "%"
                )
            );
        };
    }

    public static Specification<Movie> conjunction() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

}
