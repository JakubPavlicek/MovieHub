package com.moviehub.mapper.dto;

import com.moviehub.dto.MovieUserRating;
import com.moviehub.entity.MovieRating;

public class MovieRatingMapper {

    private MovieRatingMapper() {
    }

    public static MovieUserRating mapToRating(MovieRating movieRating) {
        return MovieUserRating.builder()
                              .rating(movieRating.getRating())
                              .build();
    }

}
