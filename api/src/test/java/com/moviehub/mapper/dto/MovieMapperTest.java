package com.moviehub.mapper.dto;

import com.moviehub.dto.AddMovieRequest;
import com.moviehub.dto.MovieCastRequest;
import com.moviehub.dto.MovieDetailsResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.MoviePreviewResponse;
import com.moviehub.dto.MovieUserRating;
import com.moviehub.dto.UpdateMovieRequest;
import com.moviehub.dto.YearListResponse;
import com.moviehub.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MovieMapperTest {

    public static final String DIRECTOR = "Christopher Nolan";
    public static final List<String> GENRES = List.of("Science Fiction", "Thriller");
    public static final List<String> COUNTRIES = List.of("USA", "UK");
    public static final List<String> PRODUCTION_COMPANIES = List.of("Warner Bros.", "Syncopy");

    @Test
    void shouldMapAddMovieRequestToMovie() {
        AddMovieRequest addMovieRequest = createAddMovieRequest();

        Movie movie = MovieMapper.mapToMovie(addMovieRequest);

        assertThat(movie).isNotNull();
        assertThat(movie.getName()).isEqualTo(addMovieRequest.getName());
        assertThat(movie.getFilename()).isEqualTo(addMovieRequest.getFilename());
        assertThat(movie.getReleaseDate()).isEqualTo(addMovieRequest.getReleaseDate());
        assertThat(movie.getDuration()).isEqualTo(addMovieRequest.getDuration());
        assertThat(movie.getDescription()).isEqualTo(addMovieRequest.getDescription());
        assertThat(movie.getPosterUrl()).isEqualTo(addMovieRequest.getPosterUrl());
        assertThat(movie.getTrailerUrl()).isEqualTo(addMovieRequest.getTrailerUrl());
        assertThat(movie.getDirector().getName()).isEqualTo(addMovieRequest.getDirector());
        assertThat(movie.getGenres()).isNotNull().hasSize(2);
        assertThat(movie.getCast()).isNotNull().hasSize(2);
        assertThat(movie.getProduction()).isNotNull().hasSize(2);
        assertThat(movie.getCountries()).isNotNull().hasSize(2);
    }

    @Test
    void shouldMapUpdateMovieRequestToMovie() {
        UpdateMovieRequest updateMovieRequest = createUpdateMovieRequest();

        Movie movie = MovieMapper.mapToMovie(updateMovieRequest);

        assertThat(movie).isNotNull();
        assertThat(movie.getName()).isEqualTo(updateMovieRequest.getName());
        assertThat(movie.getFilename()).isEqualTo(updateMovieRequest.getFilename());
        assertThat(movie.getReleaseDate()).isEqualTo(updateMovieRequest.getReleaseDate());
        assertThat(movie.getDuration()).isEqualTo(updateMovieRequest.getDuration());
        assertThat(movie.getDescription()).isEqualTo(updateMovieRequest.getDescription());
        assertThat(movie.getPosterUrl()).isEqualTo(updateMovieRequest.getPosterUrl());
        assertThat(movie.getTrailerUrl()).isEqualTo(updateMovieRequest.getTrailerUrl());
        assertThat(movie.getDirector().getName()).isEqualTo(updateMovieRequest.getDirector());
        assertThat(movie.getGenres()).isNotNull().hasSize(2);
        assertThat(movie.getCast()).isNotNull().hasSize(2);
        assertThat(movie.getProduction()).isNotNull().hasSize(2);
        assertThat(movie.getCountries()).isNotNull().hasSize(2);
    }

    @Test
    void shouldMapMovieToMoviePreviewResponse() {
        Movie movie = createMovie();

        MoviePreviewResponse response = MovieMapper.mapToMoviePreviewResponse(movie);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(movie.getId());
        assertThat(response.getName()).isEqualTo(movie.getName());
        assertThat(response.getReleaseYear()).isEqualTo(movie.getReleaseDate().getYear());
        assertThat(response.getDuration()).isEqualTo(movie.getDuration());
        assertThat(response.getPosterUrl()).isEqualTo(movie.getPosterUrl());
        assertThat(response.getGenres()).isNotNull().hasSize(2);
    }

    @Test
    void shouldMapMovieToMovieDetailsResponse() {
        Movie movie = createMovie();

        MovieDetailsResponse response = MovieMapper.mapToMovieDetailsResponse(movie);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(movie.getId());
        assertThat(response.getName()).isEqualTo(movie.getName());
        assertThat(response.getReleaseDate()).isEqualTo(movie.getReleaseDate());
        assertThat(response.getDuration()).isEqualTo(movie.getDuration());
        assertThat(response.getDescription()).isEqualTo(movie.getDescription());
        assertThat(response.getRating()).isEqualTo(movie.getRating());
        assertThat(response.getReviewCount()).isEqualTo(movie.getReviewCount());
        assertThat(response.getPosterUrl()).isEqualTo(movie.getPosterUrl());
        assertThat(response.getTrailerUrl()).isEqualTo(movie.getTrailerUrl());
        assertThat(response.getDirector().getName()).isEqualTo(movie.getDirector().getName());
        assertThat(response.getGenres()).isNotNull().hasSize(2);
        assertThat(response.getCast()).isNotNull().hasSize(2);
        assertThat(response.getProductionCompanies()).isNotNull().hasSize(2);
        assertThat(response.getCountries()).isNotNull().hasSize(2);
    }

    @Test
    void shouldMapPageToMoviePage() {
        List<Movie> movieList = List.of(createMovie(), createMovie());
        Page<Movie> moviePage = new PageImpl<>(movieList, PageRequest.of(0, 10), 2);

        MoviePage response = MovieMapper.mapToMoviePage(moviePage);

        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty().hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(moviePage.getTotalElements());
        assertThat(response.getTotalPages()).isEqualTo(moviePage.getTotalPages());
        assertThat(response.getLast()).isEqualTo(moviePage.isLast());
    }

    @Test
    void shouldMapYearsToYearListResponse() {
        List<Integer> years = List.of(2020, 2021, 2022);

        YearListResponse response = MovieMapper.mapToYearListResponse(years);

        assertThat(response).isNotNull();
        assertThat(response.getYears()).containsExactly(2020, 2021, 2022);
    }

    @Test
    void shouldMapRatingToMovieUserRating() {
        Double rating = 8.5;

        MovieUserRating response = MovieMapper.mapToMovieUserRating(rating);

        assertThat(response).isNotNull();
        assertThat(response.getRating()).isEqualTo(rating);
    }

    private AddMovieRequest createAddMovieRequest() {
        return AddMovieRequest.builder()
                              .name("Inception")
                              .filename("inception.mp4")
                              .releaseDate(LocalDate.of(2010, 7, 16))
                              .duration(148)
                              .description("A thief who steals corporate secrets through the use of dream-sharing technology.")
                              .posterUrl("https://example.com/poster.jpg")
                              .trailerUrl("https://example.com/trailer.mp4")
                              .director(DIRECTOR)
                              .cast(List.of(createMovieCastRequest(), createMovieCastRequest()))
                              .productionCompanies(PRODUCTION_COMPANIES)
                              .genres(GENRES)
                              .countries(COUNTRIES)
                              .build();
    }

    private UpdateMovieRequest createUpdateMovieRequest() {
        return UpdateMovieRequest.builder()
                                 .name("Inception Updated")
                                 .filename("inception_updated.mp4")
                                 .releaseDate(LocalDate.of(2010, 7, 17))
                                 .duration(150)
                                 .description("An updated description.")
                                 .posterUrl("https://example.com/poster_updated.jpg")
                                 .trailerUrl("https://example.com/trailer_updated.mp4")
                                 .director("Christopher Nolan Updated")
                                 .cast(List.of(createMovieCastRequest(), createMovieCastRequest()))
                                 .productionCompanies(PRODUCTION_COMPANIES)
                                 .genres(List.of("Science Fiction", "Action"))
                                 .countries(COUNTRIES)
                                 .build();
    }

    private Movie createMovie() {
        return Movie.builder()
                    .id(UUID.randomUUID())
                    .name("Inception")
                    .releaseDate(LocalDate.of(2010, 7, 16))
                    .duration(148)
                    .description("A thief who steals corporate secrets through the use of dream-sharing technology.")
                    .rating(8.8)
                    .reviewCount(1000)
                    .posterUrl("https://example.com/poster.jpg")
                    .trailerUrl("https://example.com/trailer.mp4")
                    .director(DirectorMapper.mapToDirector(DIRECTOR))
                    .cast(MovieCastMapper.mapToMovieCasts(List.of(createMovieCastRequest(), createMovieCastRequest())))
                    .production(ProductionCompanyMapper.mapToProductionCompanies(PRODUCTION_COMPANIES))
                    .genres(GenreMapper.mapToGenres(GENRES))
                    .countries(CountryMapper.mapToCountries(COUNTRIES))
                    .build();
    }

    private MovieCastRequest createMovieCastRequest() {
        return MovieCastRequest.builder()
                               .name("Leonardo DiCaprio")
                               .role("Cobb")
                               .build();
    }

}