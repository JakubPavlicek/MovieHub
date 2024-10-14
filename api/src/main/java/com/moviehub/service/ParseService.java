package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.mapper.entity.CommentFieldMapper;
import com.moviehub.mapper.entity.MovieFieldMapper;
import com.moviehub.specification.MovieSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Service
public class ParseService {

    private static final String COMMA_DELIMITER = ",";
    private static final String DOT_DELIMITER = "\\.";
    private static final String PARAM_ALL = "all";

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

    public Sort parseMovieSort(String sort) {
        return parseSort(sort, MovieFieldMapper::mapToMovieField);
    }

    public Sort parseCommentSort(String sort) {
        return parseSort(sort, CommentFieldMapper::mapToCommentField);
    }

    private <T> Specification<Movie> parseFilter(String filter, Function<List<T>, Specification<Movie>> specFunction, Function<String, T> mapper) {
        if (filter.equals(PARAM_ALL)) {
            return MovieSpecification.conjunction();
        }

        List<T> filters = Arrays.stream(filter.split(COMMA_DELIMITER))
                                .map(mapper)
                                .toList();

        return specFunction.apply(filters);
    }

    public Specification<Movie> parseReleaseYear(String releaseYear) {
        return parseFilter(releaseYear, MovieSpecification::releaseYearIn, Integer::parseInt);
    }

    public Specification<Movie> parseGenre(String genre) {
        return parseFilter(genre, MovieSpecification::genreIn, Function.identity());
    }

    public Specification<Movie> parseCountry(String country) {
        return parseFilter(country, MovieSpecification::countryIn, Function.identity());
    }


}
