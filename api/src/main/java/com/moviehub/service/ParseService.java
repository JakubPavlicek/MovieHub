package com.moviehub.service;

import com.moviehub.entity.Movie;
import com.moviehub.exception.FilterException;
import com.moviehub.exception.ParseException;
import com.moviehub.exception.SortException;
import com.moviehub.mapper.entity.CommentFieldMapper;
import com.moviehub.mapper.entity.MovieFieldMapper;
import com.moviehub.specification.MovieSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParseService {

    private static final String MOVIE_SORT_REGEX = "(name|releaseDate|duration|description|director).(asc|desc)(?:,|$)";
    private static final Pattern MOVIE_SORT_PATTERN = Pattern.compile(MOVIE_SORT_REGEX);

    private static final String COMMENT_SORT_REGEX = "(createdAt).(asc|desc)";
    private static final Pattern COMMENT_SORT_PATTERN = Pattern.compile(COMMENT_SORT_REGEX);

    private Sort parseSort(String sort, Pattern pattern, UnaryOperator<String> fieldMapper) {
        Matcher matcher = pattern.matcher(sort);
        List<Sort.Order> orders = new ArrayList<>();

        while (matcher.find()) {
            String field = fieldMapper.apply(matcher.group(1));
            Sort.Direction direction = Sort.Direction.fromString(matcher.group(2));
            orders.add(new Sort.Order(direction, field));
        }

        if (orders.isEmpty()) {
            throw new SortException("Invalid sort value: " + sort);
        }

        return Sort.by(orders);
    }

    public Sort parseMovieSort(String sort) {
        return parseSort(sort, MOVIE_SORT_PATTERN, MovieFieldMapper::mapToMovieField);
    }

    public Sort parseCommentSort(String sort) {
        return parseSort(sort, COMMENT_SORT_PATTERN, CommentFieldMapper::mapToCommentField);
    }

    private Specification<Movie> parseFilter(String filter, Map<String, Function<String, Specification<Movie>>> criteriaHandlers) {
        String[] parts = filter.split(":", 2);

        if (parts.length != 2) {
            throw new FilterException("Invalid filter format: " + filter);
        }

        String criteria = parts[0];
        String value = parts[1];

        Function<String, Specification<Movie>> handler = criteriaHandlers.get(criteria);

        if (handler == null) {
            throw new FilterException("Invalid criteria '" + criteria + "' in filter: " + filter);
        }

        return handler.apply(value);
    }

    private Specification<Movie> parseFilterList(List<String> filters, Map<String, Function<String, Specification<Movie>>> criteriaHandlers) {
        // initialize with a valid specification
        Specification<Movie> specification = MovieSpecification.conjunction();

        for (String filter : filters) {
            Specification<Movie> filterSpec = parseFilter(filter, criteriaHandlers);
            specification = specification.and(filterSpec);
        }

        return specification;
    }

    public Specification<Movie> parseName(String name) {
        return parseFilter(name, Map.of(
            "eq",   MovieSpecification::nameEqualTo,
            "like", MovieSpecification::nameContains
        ));
    }

    public Specification<Movie> parseReleaseDate(String release) {
        return parseFilter(release, Map.of(
            "eq",  value -> MovieSpecification.releaseDateEqualTo(parseDate(value)),
            "neq", value -> MovieSpecification.releaseDateNotEqualTo(parseDate(value)),
            "lt",  value -> MovieSpecification.releaseDateBefore(parseDate(value)),
            "gt",  value -> MovieSpecification.releaseDateAfter(parseDate(value)),
            "lte", value -> MovieSpecification.releaseDateBeforeOrEqualTo(parseDate(value)),
            "gte", value -> MovieSpecification.releaseDateAfterOrEqualTo(parseDate(value))
        ));
    }

    public Specification<Movie> parseDuration(String duration) {
        return parseFilter(duration, Map.of(
            "eq",  value -> MovieSpecification.durationEqualTo(parseInteger(value)),
            "neq", value -> MovieSpecification.durationNotEqualTo(parseInteger(value)),
            "lt",  value -> MovieSpecification.durationLessThan(parseInteger(value)),
            "gt",  value -> MovieSpecification.durationGreaterThan(parseInteger(value)),
            "lte", value -> MovieSpecification.durationLessThanOrEqualTo(parseInteger(value)),
            "gte", value -> MovieSpecification.durationGreaterThanOrEqualTo(parseInteger(value))
        ));
    }

    public Specification<Movie> parseDescription(String description) {
        return parseFilter(description, Map.of(
            "eq",   MovieSpecification::descriptionEqualTo,
            "like", MovieSpecification::descriptionContains
        ));
    }

    public Specification<Movie> parseRating(String rating) {
        return parseFilter(rating, Map.of(
            "eq",  value -> MovieSpecification.ratingEqualTo(parseDouble(value)),
            "lt",  value -> MovieSpecification.ratingLessThan(parseDouble(value)),
            "gt",  value -> MovieSpecification.ratingGreaterThan(parseDouble(value)),
            "lte", value -> MovieSpecification.ratingLessThanOrEqualTo(parseDouble(value)),
            "gte", value -> MovieSpecification.ratingGreaterThanOrEqualTo(parseDouble(value))
        ));
    }

    public Specification<Movie> parseReviewCount(String reviewCount) {
        return parseFilter(reviewCount, Map.of(
            "eq",  value -> MovieSpecification.reviewCountEqualTo(parseInteger(value)),
            "lt",  value -> MovieSpecification.reviewCountLessThan(parseInteger(value)),
            "gt",  value -> MovieSpecification.reviewCountGreaterThan(parseInteger(value)),
            "lte", value -> MovieSpecification.reviewCountLessThanOrEqualTo(parseInteger(value)),
            "gte", value -> MovieSpecification.reviewCountGreaterThanOrEqualTo(parseInteger(value))
        ));
    }

    public Specification<Movie> parseDirector(String director) {
        return parseFilter(director, Map.of(
            "eq",   MovieSpecification::directorEqualTo,
            "like", MovieSpecification::directorContains
        ));
    }

    public Specification<Movie> parseActors(List<String> actors) {
        return parseFilterList(actors, Map.of(
            "eq",   MovieSpecification::actorEqualTo,
            "like", MovieSpecification::actorContains
        ));
    }

    public Specification<Movie> parseGenres(List<String> genres) {
        return parseFilterList(genres, Map.of(
            "eq", MovieSpecification::genreEqualTo,
            "like", MovieSpecification::genreContains
        ));
    }

    public Specification<Movie> parseCountries(List<String> countries) {
        return parseFilterList(countries, Map.of(
            "eq", MovieSpecification::countryEqualTo,
            "like", MovieSpecification::countryContains
        ));
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new ParseException("Invalid date format: " + date);
        }
    }

    private Integer parseInteger(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException exception) {
            throw new ParseException("Invalid integer format: " + integer);
        }
    }

    private Double parseDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException exception) {
            throw new ParseException("Invalid double format: " + number);
        }
    }

}
