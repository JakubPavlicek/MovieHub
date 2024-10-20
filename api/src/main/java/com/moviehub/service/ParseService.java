package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.mapper.entity.CommentFieldMapper;
import com.moviehub.mapper.entity.MovieFieldMapper;
import com.moviehub.specification.MovieSpecification;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for parsing sorting and filtering parameters for movies and comments.
@Service
@Log4j2
public class ParseService {

    /// Comma delimiter used for separating values in sorting and filtering parameters.
    private static final String COMMA_DELIMITER = ",";
    /// Dot delimiter used for separating field names and sorting directions.
    private static final String DOT_DELIMITER = "\\.";
    /// Constant representing the parameter value for selecting all available items in filtering.
    private static final String PARAM_ALL = "all";

    /// Parses the sorting criteria and returns a Sort object.
    ///
    /// @param sort The sorting criteria as a comma-separated string (e.g., "field1.direction,field2.direction").
    /// @param fieldMapper A function that maps field names to their corresponding database fields.
    /// @return A Sort object representing the parsed sorting criteria.
    private Sort parseSort(String sort, UnaryOperator<String> fieldMapper) {
        List<Sort.Order> orders = new ArrayList<>();

        String[] sortValues = sort.split(COMMA_DELIMITER);

        for (String sortValue : sortValues) {
            String[] values = sortValue.split(DOT_DELIMITER);

            String field = fieldMapper.apply(values[0]);
            Sort.Direction direction = Sort.Direction.fromString(values[1]);

            orders.add(new Sort.Order(direction, field));
        }

        return Sort.by(orders);
    }

    /// Parses the sorting criteria for movies and returns a Sort object.
    ///
    /// @param sort The sorting criteria as a comma-separated string.
    /// @return A Sort object representing the parsed movie sorting criteria.
    public Sort parseMovieSort(String sort) {
        log.debug("parsing movie sort: {}", sort);
        return parseSort(sort, MovieFieldMapper::mapToMovieField);
    }

    /// Parses the sorting criteria for comments and returns a Sort object.
    ///
    /// @param sort The sorting criteria as a comma-separated string.
    /// @return A Sort object representing the parsed comment sorting criteria.
    public Sort parseCommentSort(String sort) {
        log.debug("parsing comment sort: {}", sort);
        return parseSort(sort, CommentFieldMapper::mapToCommentField);
    }

    /// Parses the filter criteria and returns a Specification for movies.
    ///
    /// @param filter The filter criteria as a comma-separated string.
    /// @param specFunction A function that takes a list of filter values and returns a Specification.
    /// @param mapper A function that maps a string filter value to the desired type.
    /// @param <T> The type of the filter value.
    /// @return A Specification representing the parsed filter criteria.
    private <T> Specification<Movie> parseFilter(String filter, Function<List<T>, Specification<Movie>> specFunction, Function<String, T> mapper) {
        if (filter.equals(PARAM_ALL)) {
            return MovieSpecification.conjunction();
        }

        List<T> filters = Arrays.stream(filter.split(COMMA_DELIMITER))
                                .map(mapper)
                                .toList();

        return specFunction.apply(filters);
    }

    /// Parses the release year filter and returns a Specification for movies.
    ///
    /// @param releaseYear The release year filter as a comma-separated string of integers.
    /// @return A Specification representing the parsed release year criteria.
    public Specification<Movie> parseReleaseYear(String releaseYear) {
        log.debug("parsing release year: {}", releaseYear);
        return parseFilter(releaseYear, MovieSpecification::releaseYearIn, Integer::parseInt);
    }

    /// Parses the genre filter and returns a Specification for movies.
    ///
    /// @param genre The genre filter as a comma-separated string.
    /// @return A Specification representing the parsed genre criteria.
    public Specification<Movie> parseGenre(String genre) {
        log.debug("parsing genre: {}", genre);
        return parseFilter(genre, MovieSpecification::genreIn, Function.identity());
    }

    /// Parses the country filter and returns a Specification for movies.
    ///
    /// @param country The country filter as a comma-separated string.
    /// @return A Specification representing the parsed country criteria.
    public Specification<Movie> parseCountry(String country) {
        log.debug("parsing country: {}", country);
        return parseFilter(country, MovieSpecification::countryIn, Function.identity());
    }


}
