package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.exception.SortException;
import com.moviehub.mapper.entity.CommentFieldMapper;
import com.moviehub.mapper.entity.MovieFieldMapper;
import com.moviehub.specification.MovieSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        if (orders.isEmpty()) {
            throw new SortException("Invalid sort value: " + sort);
        }

        return Sort.by(orders);
    }

    public Sort parseMovieSort(String sort) {
        return parseSort(sort, MovieFieldMapper::mapToMovieField);
    }

    public Sort parseCommentSort(String sort) {
        return parseSort(sort, CommentFieldMapper::mapToCommentField);
    }

    private Specification<Movie> parseFilter(String filter, Function<String, Specification<Movie>> specificationFunction) {
        String[] filters = filter.split(COMMA_DELIMITER);

        Specification<Movie> specification = MovieSpecification.conjunction();

        // if the query param contains "all", do not filter the movies
        if (filters.length == 1 && filters[0].equals(PARAM_ALL)) {
            return specification;
        }

        for (String filterInQuery : filters) {
            specification.and(specificationFunction.apply(filterInQuery));
        }

        return specification;
    }

    public Specification<Movie> parseReleaseYear(String releaseYear) {
        return parseFilter(releaseYear, MovieSpecification::releaseYearEqualTo);
    }

    public Specification<Movie> parseGenre(String genre) {
        return parseFilter(genre, MovieSpecification::genreEqualTo);
    }

    public Specification<Movie> parseCountry(String country) {
        return parseFilter(country, MovieSpecification::countryEqualTo);
    }

}
