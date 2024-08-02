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

    // TODO: catch exceptions when parsing

    public static Specification<Movie> nameEqualTo(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.NAME), name);
    }

    public static Specification<Movie> nameContains(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.NAME), "%" + name + "%");
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

    public static Specification<Movie> lengthEqualTo(Integer length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.LENGTH), length);
    }

    public static Specification<Movie> lengthNotEqualTo(Integer length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(Movie_.LENGTH), length);
    }

    public static Specification<Movie> lengthLessThan(Integer length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(Movie_.LENGTH), length);
    }

    public static Specification<Movie> lengthGreaterThan(Integer length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(Movie_.LENGTH), length);
    }

    public static Specification<Movie> lengthLessThanOrEqualTo(Integer length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Movie_.LENGTH), length);
    }

    public static Specification<Movie> lengthGreaterThanOrEqualTo(Integer length) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Movie_.LENGTH), length);
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

    public static Specification<Movie> countryEqualTo(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.COUNTRIES).get(Country_.NAME), country);
    }

    public static Specification<Movie> conjunction() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

}
