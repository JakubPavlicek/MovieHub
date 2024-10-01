package com.moviehub.mapper.dto;

import com.moviehub.dto.Rating;
import com.moviehub.entity.MovieRating;

public class MovieRatingMapper {

    private MovieRatingMapper() {
    }

    public static Rating mapToRating(MovieRating movieRating) {
        return Rating.builder()
                     .rating(movieRating.getRating())
                     .build();
    }

}
