package com.moviehub;

import com.moviehub.entity.Actor;
import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentInfo;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Gender;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.entity.MovieRating;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EntityBuilder {

    private EntityBuilder() {
    }

    public static final String FILENAME = "test.mp4";
    public static final Integer DURATION = 120;
    public static final String POSTER_URL = "https://example.com/poster.jpg";
    public static final String TRAILER_URL = "https://example.com/trailer.mp4";
    public static final String DESCRIPTION = "A test movie";

    public static final String BIO = "Bio";

    private static final UUID[] CAST_UUIDS = {
        UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
        UUID.fromString("a76b9465-98d4-4b02-8f29-e5855e5cfc54"),
        UUID.fromString("0e582ab1-4c90-4c30-93a5-29f575f77efb")
    };

    private static final UUID[] GENRE_UUIDS = {
        UUID.fromString("8d6f3bfa-93de-41f0-a654-3a93735e0d68"),
        UUID.fromString("14ac6b61-8057-4be5-8b2c-d6b7b4f501c3")
    };

    private static final UUID[] COUNTRY_UUIDS = {
        UUID.fromString("e6720e43-b689-4f76-8cf6-3d474d8c9d39"),
        UUID.fromString("50f3e074-ff36-4db3-b4c4-ecacb82951bb")
    };

    private static final UUID[] PRODUCTION_COMPANY_UUIDS = {
        UUID.fromString("e45d9e2d-fb94-4e45-8985-d88d32c0544f"),
        UUID.fromString("0b5a2b8c-f98e-49e3-bd5b-377a22b9e093")
    };

    public static Movie createMovie(String name) {
        return Movie.builder()
                    .name(name)
                    .filename(FILENAME)
                    .releaseDate(LocalDate.of(2024, 1, 1))
                    .duration(DURATION)
                    .description(DESCRIPTION)
                    .posterUrl(POSTER_URL)
                    .trailerUrl(TRAILER_URL)
                    .build();
    }

    public static Movie createMovieWithYear(String name, int year) {
        return Movie.builder()
                    .name(name)
                    .filename(FILENAME)
                    .releaseDate(LocalDate.of(year, 1, 1))
                    .duration(DURATION)
                    .description(DESCRIPTION)
                    .posterUrl(POSTER_URL)
                    .trailerUrl(TRAILER_URL)
                    .build();
    }

    public static Director createDirector(String name) {
        return Director.builder()
                       .name(name)
                       .bio(BIO)
                       .gender(Gender.MALE)
                       .build();
    }

    public static Actor createActor(String name) {
        return Actor.builder()
                    .name(name)
                    .bio(BIO)
                    .gender(Gender.MALE)
                    .build();
    }

    public static User createUser(String name) {
        return User.builder()
                   .id("auth0|" + name)
                   .name(name)
                   .email(name + "@example.com")
                   .pictureUrl("https://example.com/picture.jpg")
                   .build();
    }

    public static MovieRating createMovieRating(Movie movie, User user, double rating) {
        return MovieRating.builder()
                          .movie(movie)
                          .user(user)
                          .rating(rating)
                          .build();
    }

    public static CommentReaction createCommentReaction(Comment comment, User user, ReactionType reactionType) {
        return CommentReaction.builder()
                              .commentInfo(comment)
                              .user(user)
                              .reactionType(reactionType)
                              .build();
    }

    public static Genre createGenre(String name) {
        return Genre.builder()
                    .name(name)
                    .build();
    }

    public static Country createCountry(String name) {
        return Country.builder()
                      .name(name)
                      .build();
    }

    public static ProductionCompany createProductionCompany(String name) {
        return ProductionCompany.builder()
                                .name(name)
                                .build();
    }

    public static MovieCast createMovieCast(Movie movie, Actor actor) {
        return MovieCast.builder()
                        .movie(movie)
                        .actor(actor)
                        .role("role")
                        .build();
    }

    public static CommentInfo createComment(Movie movie, User user, String text) {
        Comment comment = new Comment();
        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText(text);
        return comment;
    }

    public static CommentInfo createReply(Comment comment, User user, String text) {
        CommentReply reply = new CommentReply();
        reply.setComment(comment);
        reply.setUser(user);
        reply.setText(text);
        return reply;
    }

    public static Movie createMovie() {
        return Movie.builder()
                    .id(UUID.fromString("24eadcf9-353c-4d12-82a3-441cbf177897"))
                    .name("Movie")
                    .releaseDate(LocalDate.of(2023, 1, 1))
                    .duration(120)
                    .description("A test movie.")
                    .posterUrl("https://example.com/poster.jpg")
                    .trailerUrl("https://example.com/trailer.mp4")
                    .director(createDirector("Director"))
                    .cast(createMovieCasts())
                    .production(createProductionCompanies())
                    .countries(createCountries())
                    .genres(createGenres())
                    .build();
    }

    public static Movie createIncomingMovieWithEmptyLists() {
        return Movie.builder()
                    .id(UUID.fromString("29bcc401-925b-4770-93fb-bbeb9173b8d6"))
                    .name("New Movie")
                    .releaseDate(LocalDate.of(2024, 1, 1))
                    .duration(150)
                    .description("A new movie.")
                    .posterUrl("https://example.com/posterImage.jpg")
                    .trailerUrl("https://example.com/trailerVideo.mp4")
                    .director(createDirector("Director"))
                    .cast(Collections.emptyList())
                    .production(Collections.emptyList())
                    .countries(Collections.emptyList())
                    .genres(Collections.emptyList())
                    .build();
    }

    public static Comment createComment() {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setText("This is a test comment.");
        return comment;
    }

    public static List<MovieCast> createMovieCasts() {
        List<MovieCast> casts = new ArrayList<>();
        for (UUID castUuid : CAST_UUIDS) {
            MovieCast cast = new MovieCast();
            cast.setId(castUuid);
            cast.setActor(createActor("Actor " + castUuid));
            casts.add(cast);
        }
        return casts;
    }

    public static List<Genre> createGenres() {
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < GENRE_UUIDS.length; i++) {
            Genre genre = new Genre();
            genre.setId(GENRE_UUIDS[i]);
            genre.setName("Genre " + (i + 1));
            genres.add(genre);
        }
        return genres;
    }

    public static List<Country> createCountries() {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < COUNTRY_UUIDS.length; i++) {
            Country country = new Country();
            country.setId(COUNTRY_UUIDS[i]);
            country.setName("Country " + (i + 1));
            countries.add(country);
        }
        return countries;
    }

    public static List<ProductionCompany> createProductionCompanies() {
        List<ProductionCompany> companies = new ArrayList<>();
        for (int i = 0; i < PRODUCTION_COMPANY_UUIDS.length; i++) {
            ProductionCompany company = new ProductionCompany();
            company.setId(PRODUCTION_COMPANY_UUIDS[i]);
            company.setName("Company " + (i + 1));
            companies.add(company);
        }
        return companies;
    }

}
