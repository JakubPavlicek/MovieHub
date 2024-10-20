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

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Specification class for querying Movie entities.
/// Provides methods to create specifications based on various criteria.
public class MovieSpecification {

    /// Private constructor to prevent instantiation
    private MovieSpecification() {
    }

    /// Creates a specification to filter movies by a list of genres.
    /// @param genres List of genre names to filter by.
    /// @return A Specification for filtering movies by genre.
    public static Specification<Movie> genreIn(List<String> genres) {
        return (root, query, cb) -> root.get(Movie_.GENRES).get(Genre_.NAME).in(genres);
    }

    /// Creates a specification to filter movies by a list of countries.
    /// @param countries List of country names to filter by.
    /// @return A Specification for filtering movies by country.
    public static Specification<Movie> countryIn(List<String> countries) {
        return (root, query, cb) -> root.get(Movie_.COUNTRIES).get(Country_.NAME).in(countries);
    }

    /// Creates a specification to filter movies by a list of release years.
    /// @param years List of years to filter by.
    /// @return A Specification for filtering movies by release year.
    public static Specification<Movie> releaseYearIn(List<Integer> years) {
        return (root, query, cb) -> cb.function("DATE_PART", Integer.class, cb.literal("year"), root.get(Movie_.RELEASE_DATE)).in(years);
    }

    /// Creates a specification to search movies by a keyword.
    /// The search checks the movie name, director name, actor name, and production company name.
    /// @param keyword The keyword to search for.
    /// @return A Specification for searching movies by keyword.
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

    /// Creates a specification that always evaluates to true.
    /// @return A Specification that represents a conjunction (always true).
    public static Specification<Movie> conjunction() {
        return (root, query, cb) -> cb.conjunction();
    }

}
