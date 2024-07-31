package com.movie_manager.specification;

import com.movie_manager.entity.Actor_;
import com.movie_manager.entity.Country_;
import com.movie_manager.entity.Director_;
import com.movie_manager.entity.Genre_;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.Movie_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MovieSpecification {

    private MovieSpecification() {
    }

    public static Specification<Movie> nameEqualTo(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.NAME), name);
    }

    public static Specification<Movie> nameContains(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.NAME), "%" + name + "%");
    }

    public static Specification<Movie> releaseEqualTo(String release) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.RELEASE), LocalDate.parse(release));
    }

    public static Specification<Movie> releaseBefore(String release) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(Movie_.RELEASE), LocalDate.parse(release));
    }

    public static Specification<Movie> releaseAfter(String release) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(Movie_.RELEASE), LocalDate.parse(release));
    }

    public static Specification<Movie> lengthEqualTo(String length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.LENGTH), Integer.parseInt(length));
    }

    public static Specification<Movie> lengthNotEqualTo(String length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(Movie_.LENGTH), Integer.parseInt(length));
    }

    public static Specification<Movie> lengthLessThan(String length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(Movie_.LENGTH), Integer.parseInt(length));
    }

    public static Specification<Movie> lengthGreaterThan(String length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(Movie_.LENGTH), Integer.parseInt(length));
    }

    public static Specification<Movie> lengthLessThanOrEqualTo(String length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Movie_.LENGTH), Integer.parseInt(length));
    }

    public static Specification<Movie> lengthGreaterThanOrEqualTo(String length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Movie_.LENGTH), Integer.parseInt(length));
    }

    public static Specification<Movie> descriptionEqualTo(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.DESCRIPTION), description);
    }

    public static Specification<Movie> descriptionContains(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.DESCRIPTION), "%" + description + "%");
    }

    public static Specification<Movie> directorEqualTo(String director) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.DIRECTOR).get(Director_.NAME), director);
    }

    public static Specification<Movie> directorContains(String director) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.DIRECTOR).get(Director_.NAME), "%" + director + "%");
    }

    public static Specification<Movie> actorEqualTo(String actor) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.ACTORS).get(Actor_.NAME), actor);
    }

    public static Specification<Movie> actorContains(String actor) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.ACTORS).get(Actor_.NAME), "%" + actor + "%");
    }

    public static Specification<Movie> genreEqualTo(String genre) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.GENRES).get(Genre_.NAME), genre);
    }

    public static Specification<Movie> genreContains(String genre) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.GENRES).get(Genre_.NAME), "%" + genre + "%");
    }

    public static Specification<Movie> countryEqualTo(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.COUNTRIES).get(Country_.NAME), country);
    }

    public static Specification<Movie> countryContains(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.COUNTRIES).get(Country_.NAME), "%" + country + "%");
    }

    public static Specification<Movie> conjunction() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

}
