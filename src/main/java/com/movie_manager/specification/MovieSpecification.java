package com.movie_manager.specification;

import com.movie_manager.entity.Movie;
import com.movie_manager.entity.Movie_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MovieSpecification {

    private MovieSpecification() {
    }

    public static Specification<Movie> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.NAME), name);
    }

    public static Specification<Movie> likeName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Movie_.NAME), "%" + name + "%");
    }

    public static Specification<Movie> hasRelease(String release) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Movie_.RELEASE), LocalDate.parse(release));
    }

    public static Specification<Movie> beforeRelease(String release) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(Movie_.RELEASE), LocalDate.parse(release));
    }

    public static Specification<Movie> afterRelease(String release) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(Movie_.RELEASE), LocalDate.parse(release));
    }

}
