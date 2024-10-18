package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.Actor;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Comment_;
import com.moviehub.entity.Country;
import com.moviehub.entity.Director;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.MovieCast;
import com.moviehub.entity.Movie_;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.entity.User;
import com.moviehub.exception.MovieNotFoundException;
import com.moviehub.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createActor;
import static com.moviehub.EntityBuilder.createComment;
import static com.moviehub.EntityBuilder.createCountry;
import static com.moviehub.EntityBuilder.createDirector;
import static com.moviehub.EntityBuilder.createGenre;
import static com.moviehub.EntityBuilder.createMovieCast;
import static com.moviehub.EntityBuilder.createProductionCompany;
import static com.moviehub.EntityBuilder.createUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
@Import(SecurityConfig.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MovieService movieService;

    private static final UUID MOVIE_ID = UUID.fromString("16e5572e-3486-4552-99a9-ccf12d0d4ab9");
    private static final UUID DIRECTOR_ID = UUID.fromString("2fe45e05-30f1-4b2c-a5cf-60f4ac21de72");
    private static final UUID ACTOR_ID = UUID.fromString("7f9805fd-f3c2-4ef6-87a4-e81a010b46fb");
    private static final UUID COMPANY_ID = UUID.fromString("19ffba27-2c19-442a-acd4-c428043a695c");
    private static final UUID GENRE_ID = UUID.fromString("9330b4b6-863e-4a1f-811f-796d3b29ab7e");
    private static final UUID COUNTRY_ID = UUID.fromString("69f3773b-8222-4d90-bb01-6231c3b5c39a");
    private static final UUID COMMENT_ID = UUID.fromString("22c7d8fb-21b4-40ec-b9c0-371943443df3");

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddMovie() throws Exception {
        Movie movie = createMovieWithAllDetails();

        when(movieService.addMovie(any())).thenReturn(movie);

        mvc.perform(post("/movies")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "The Conjuring",
                          "filename": "placeholder.mp4",
                          "releaseDate": "2024-07-29",
                          "duration": 112,
                          "description": "Paranormal movie.",
                          "posterUrl": "https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg",
                          "trailerUrl": "https://www.youtube.com/watch?v=k10ETZ41q5o",
                          "director": "Patrick Wilson",
                          "cast": [
                            {
                              "name": "James One",
                              "role": "Investigator"
                            }
                          ],
                          "productionCompanies": [
                            "A24"
                          ],
                          "genres": [
                            "Horror"
                          ],
                          "countries": [
                            "Italy"
                          ]
                        }
                        """))
           .andExpectAll(
               status().isCreated(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.id").value(MOVIE_ID.toString()),
               jsonPath("$.name").value("The Conjuring"),
               jsonPath("$.filename").value("placeholder.mp4"),
               jsonPath("$.releaseDate").value("2024-07-29"),
               jsonPath("$.duration").value("112"),
               jsonPath("$.description").value("Paranormal movie."),
               jsonPath("$.rating").value("0.0"),
               jsonPath("$.reviewCount").value("0"),
               jsonPath("$.posterUrl").value("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg"),
               jsonPath("$.trailerUrl").value("https://www.youtube.com/watch?v=k10ETZ41q5o"),
               jsonPath("$.director").exists(),
               jsonPath("$.director.id").value(DIRECTOR_ID.toString()),
               jsonPath("$.director.name").value("Patrick Wilson"),
               jsonPath("$.cast").exists(),
               jsonPath("$.cast[0].id").value(ACTOR_ID.toString()),
               jsonPath("$.cast[0].name").value("James One"),
               jsonPath("$.cast[0].role").value("Investigator"),
               jsonPath("$.productionCompanies").exists(),
               jsonPath("$.productionCompanies[0].id").value(COMPANY_ID.toString()),
               jsonPath("$.productionCompanies[0].name").value("A24"),
               jsonPath("$.genres").exists(),
               jsonPath("$.genres[0].id").value(GENRE_ID.toString()),
               jsonPath("$.genres[0].name").value("Horror"),
               jsonPath("$.countries").exists(),
               jsonPath("$.countries[0].id").value(COUNTRY_ID.toString()),
               jsonPath("$.countries[0].name").value("Italy")
           );
    }

    @Test
    void shouldNotAddMovieWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/movies"))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldNotAddMovieWhenUserIsHasNoAdminRole() throws Exception {
        mvc.perform(post("/movies"))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotAddMovieWhenContentIsInvalid() throws Exception {
        mvc.perform(post("/movies")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "<script>",
                          "filename": "<script>",
                          "releaseDate": "",
                          "duration": -20,
                          "description": "",
                          "posterUrl": "<script>",
                          "trailerUrl": "<script>",
                          "director": "<script>",
                          "cast": [
                            {
                              "name": "<script>",
                              "role": ""
                            }
                          ],
                          "productionCompanies": [
                            ""
                          ],
                          "genres": [
                            "<script>"
                          ],
                          "countries": [
                            "<script>"
                          ]
                        }
                        """))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.name").exists(),
               jsonPath("$.contextInfo.filename").exists(),
               jsonPath("$.contextInfo.releaseDate").exists(),
               jsonPath("$.contextInfo.duration").exists(),
               jsonPath("$.contextInfo.description").exists(),
               jsonPath("$.contextInfo.posterUrl").exists(),
               jsonPath("$.contextInfo.trailerUrl").exists(),
               jsonPath("$.contextInfo.director").exists(),
               jsonPath("$.contextInfo.['cast[0].name']").exists(),
               jsonPath("$.contextInfo.['cast[0].role']").exists(),
               jsonPath("$.contextInfo.['productionCompanies[0]']").exists(),
               jsonPath("$.contextInfo.['genres[0]']").exists(),
               jsonPath("$.contextInfo.['countries[0]']").exists()
           );
    }

    @Test
    void shouldGetMovies() throws Exception {
        Movie movie = createMoviePreview();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Movie_.NAME)
                                                      .descending());
        Page<Movie> movies = new PageImpl<>(List.of(movie), pageable, 1L);

        when(movieService.getMovies(0, 10, "name.desc")).thenReturn(movies);

        mvc.perform(get("/movies")
               .param("page", "0")
               .param("limit", "10")
               .param("sort", "name.desc"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.content").exists(),
               jsonPath("$.content[0].id").value(MOVIE_ID.toString()),
               jsonPath("$.content[0].name").value("The Conjuring"),
               jsonPath("$.content[0].releaseYear").value("2024"),
               jsonPath("$.content[0].duration").value("112"),
               jsonPath("$.content[0].posterUrl").value("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg"),
               jsonPath("$.content[0].genres[0]").value("Horror"),
               jsonPath("$.content[0].genres[1]").value("Action")
           );
    }

    @Test
    void shouldNotGetMoviesWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/movies")
               .param("page", "-1")
               .param("limit", "-1")
               .param("sort", "name"))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.page").exists(),
               jsonPath("$.contextInfo.limit").exists(),
               jsonPath("$.contextInfo.sort").exists()
           );
    }

    @Test
    void shouldFilterMovies() throws Exception {
        Movie movie = createMoviePreview();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Movie_.NAME)
                                                      .descending());
        Page<Movie> movies = new PageImpl<>(List.of(movie), pageable, 1L);

        when(movieService.filterMovies(0, 10, "name.desc", "2024", "Horror", "Italy")).thenReturn(movies);

        mvc.perform(get("/movies/filter")
               .param("page", "0")
               .param("limit", "10")
               .param("sort", "name.desc")
               .param("releaseYear", "2024")
               .param("genre", "Horror")
               .param("country", "Italy"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.content").exists(),
               jsonPath("$.content[0].id").value(MOVIE_ID.toString()),
               jsonPath("$.content[0].name").value("The Conjuring"),
               jsonPath("$.content[0].releaseYear").value("2024"),
               jsonPath("$.content[0].duration").value("112"),
               jsonPath("$.content[0].posterUrl").value("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg"),
               jsonPath("$.content[0].genres[0]").value("Horror"),
               jsonPath("$.content[0].genres[1]").value("Action")
           );
    }

    @Test
    void shouldNotFilterMoviesWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/movies/filter")
               .param("page", "-1")
               .param("limit", "-1")
               .param("sort", "name")
               .param("releaseYear", "-20")
               .param("genre", "<script>")
               .param("country", "<script>"))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.page").exists(),
               jsonPath("$.contextInfo.limit").exists(),
               jsonPath("$.contextInfo.sort").exists(),
               jsonPath("$.contextInfo.releaseYear").exists(),
               jsonPath("$.contextInfo.genre").exists(),
               jsonPath("$.contextInfo.country").exists()
           );
    }

    @Test
    void shouldSearchMovies() throws Exception {
        Movie movie = createMoviePreview();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Movie_.NAME)
                                                      .descending());
        Page<Movie> movies = new PageImpl<>(List.of(movie), pageable, 1L);

        when(movieService.searchMovies(0, 10, "name.desc", "The")).thenReturn(movies);

        mvc.perform(get("/movies/search")
               .param("page", "0")
               .param("limit", "10")
               .param("sort", "name.desc")
               .param("keyword", "The"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.content").exists(),
               jsonPath("$.content[0].id").value(MOVIE_ID.toString()),
               jsonPath("$.content[0].name").value("The Conjuring"),
               jsonPath("$.content[0].releaseYear").value("2024"),
               jsonPath("$.content[0].duration").value("112"),
               jsonPath("$.content[0].posterUrl").value("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg"),
               jsonPath("$.content[0].genres[0]").value("Horror"),
               jsonPath("$.content[0].genres[1]").value("Action")
           );
    }

    @Test
    void shouldNotSearchMoviesWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/movies/search")
               .param("page", "-1")
               .param("limit", "-1")
               .param("sort", "name")
               .param("keyword", "<script>"))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.page").exists(),
               jsonPath("$.contextInfo.limit").exists(),
               jsonPath("$.contextInfo.sort").exists(),
               jsonPath("$.contextInfo.keyword").exists()
           );
    }

    @Test
    void shouldGetYears() throws Exception {
        List<Integer> years = List.of(2024, 2022);

        when(movieService.getYears()).thenReturn(years);

        mvc.perform(get("/movies/years"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.years").isArray(),
               jsonPath("$.years[0]").value(2024),
               jsonPath("$.years[1]").value(2022)
           );
    }

    @Test
    void shouldGetMovie() throws Exception {
        Movie movie = createMovieWithAllDetails();

        when(movieService.getMovieWithDetails(MOVIE_ID)).thenReturn(movie);

        mvc.perform(get("/movies/{movieId}", MOVIE_ID))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.id").value(MOVIE_ID.toString()),
               jsonPath("$.name").value("The Conjuring"),
               jsonPath("$.filename").value("placeholder.mp4"),
               jsonPath("$.releaseDate").value("2024-07-29"),
               jsonPath("$.duration").value("112"),
               jsonPath("$.description").value("Paranormal movie."),
               jsonPath("$.rating").value("0.0"),
               jsonPath("$.reviewCount").value("0"),
               jsonPath("$.posterUrl").value("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg"),
               jsonPath("$.trailerUrl").value("https://www.youtube.com/watch?v=k10ETZ41q5o"),
               jsonPath("$.director").exists(),
               jsonPath("$.director.id").value(DIRECTOR_ID.toString()),
               jsonPath("$.director.name").value("Patrick Wilson"),
               jsonPath("$.cast").exists(),
               jsonPath("$.cast[0].id").value(ACTOR_ID.toString()),
               jsonPath("$.cast[0].name").value("James One"),
               jsonPath("$.cast[0].role").value("Investigator"),
               jsonPath("$.productionCompanies").exists(),
               jsonPath("$.productionCompanies[0].id").value(COMPANY_ID.toString()),
               jsonPath("$.productionCompanies[0].name").value("A24"),
               jsonPath("$.genres").exists(),
               jsonPath("$.genres[0].id").value(GENRE_ID.toString()),
               jsonPath("$.genres[0].name").value("Horror"),
               jsonPath("$.countries").exists(),
               jsonPath("$.countries[0].id").value(COUNTRY_ID.toString()),
               jsonPath("$.countries[0].name").value("Italy")
           );
    }

    @Test
    void shouldThrowMovieNotFoundExceptionWhenMovieDoesNotExistForMovieRetrieval() throws Exception {
        when(movieService.getMovieWithDetails(MOVIE_ID)).thenThrow(MovieNotFoundException.class);

        mvc.perform(get("/movies/{movieId}", MOVIE_ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateMovie() throws Exception {
        Movie movie = createMovieWithAllDetails();

        when(movieService.updateMovie(any(), any())).thenReturn(movie);

        mvc.perform(put("/movies/{movieId}", MOVIE_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "The Conjuring",
                          "filename": "placeholder.mp4",
                          "releaseDate": "2024-07-29",
                          "duration": 112,
                          "description": "Paranormal investigators Ed and Lorraine Warren work to help a family terrorized by a dark presence in their farmhouse.",
                          "posterUrl": "https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg",
                          "trailerUrl": "https://www.youtube.com/watch?v=k10ETZ41q5o",
                          "director": "Patrick Wilson",
                          "cast": [
                            {
                              "name": "Patrick Wilson",
                              "role": "Detective"
                            }
                          ],
                          "productionCompanies": [
                            "A24"
                          ],
                          "genres": [
                            "Horror"
                          ],
                          "countries": [
                            "Italy"
                          ]
                        }
                        """))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.id").value(MOVIE_ID.toString()),
               jsonPath("$.name").value("The Conjuring"),
               jsonPath("$.filename").value("placeholder.mp4"),
               jsonPath("$.releaseDate").value("2024-07-29"),
               jsonPath("$.duration").value("112"),
               jsonPath("$.description").value("Paranormal movie."),
               jsonPath("$.rating").value("0.0"),
               jsonPath("$.reviewCount").value("0"),
               jsonPath("$.posterUrl").value("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg"),
               jsonPath("$.trailerUrl").value("https://www.youtube.com/watch?v=k10ETZ41q5o"),
               jsonPath("$.director").exists(),
               jsonPath("$.director.id").value(DIRECTOR_ID.toString()),
               jsonPath("$.director.name").value("Patrick Wilson"),
               jsonPath("$.cast").exists(),
               jsonPath("$.cast[0].id").value(ACTOR_ID.toString()),
               jsonPath("$.cast[0].name").value("James One"),
               jsonPath("$.cast[0].role").value("Investigator"),
               jsonPath("$.productionCompanies").exists(),
               jsonPath("$.productionCompanies[0].id").value(COMPANY_ID.toString()),
               jsonPath("$.productionCompanies[0].name").value("A24"),
               jsonPath("$.genres").exists(),
               jsonPath("$.genres[0].id").value(GENRE_ID.toString()),
               jsonPath("$.genres[0].name").value("Horror"),
               jsonPath("$.countries").exists(),
               jsonPath("$.countries[0].id").value(COUNTRY_ID.toString()),
               jsonPath("$.countries[0].name").value("Italy")
           );
    }

    @Test
    void shouldNotUpdateMovieWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(put("/movies/{movieId}", MOVIE_ID))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldNotUpdateMovieWhenUserHasNoAdminRole() throws Exception {
        mvc.perform(put("/movies/{movieId}", MOVIE_ID))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldThrowMovieNotFoundExceptionWhenMovieDoesNotExistForUpdate() throws Exception {
        when(movieService.updateMovie(any(), any())).thenThrow(MovieNotFoundException.class);

        mvc.perform(put("/movies/{movieId}", MOVIE_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "The Conjuring",
                          "filename": "placeholder.mp4",
                          "releaseDate": "2024-07-29",
                          "duration": 112,
                          "description": "Paranormal investigators Ed and Lorraine Warren work to help a family terrorized by a dark presence in their farmhouse.",
                          "posterUrl": "https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg",
                          "trailerUrl": "https://www.youtube.com/watch?v=k10ETZ41q5o",
                          "director": "Patrick Wilson",
                          "cast": [
                            {
                              "name": "Patrick Wilson",
                              "role": "Detective"
                            }
                          ],
                          "productionCompanies": [
                            "A24"
                          ],
                          "genres": [
                            "Horror"
                          ],
                          "countries": [
                            "Italy"
                          ]
                        }
                        """))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteMovie() throws Exception {
        mvc.perform(delete("/movies/{movieId}", MOVIE_ID))
           .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteMovieWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(delete("/movies/{movieId}", MOVIE_ID))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldNotDeleteMovieWhenUserIsHasNoAdminRole() throws Exception {
        mvc.perform(delete("/movies/{movieId}", MOVIE_ID))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldThrowMovieNotFoundExceptionWhenMovieDoesNotExistForDelete() throws Exception {
        doThrow(MovieNotFoundException.class).when(movieService)
                                             .deleteMovie(MOVIE_ID);

        mvc.perform(delete("/movies/{movieId}", MOVIE_ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser
    void shouldAddRating() throws Exception {
        mvc.perform(post("/movies/{movieId}/ratings", MOVIE_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "rating": 8
                        }
                        """))
           .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotAddRatingWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/movies/{movieId}/ratings", MOVIE_ID))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void shouldNotAddRatingWhenUserHasNoUserOrAdminRole() throws Exception {
        mvc.perform(post("/movies/{movieId}/ratings", MOVIE_ID))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldNotAddRatingWhenContentIsInvalid() throws Exception {
        mvc.perform(post("/movies/{movieId}/ratings", MOVIE_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "rating": -2
                        }
                        """))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.rating").exists()
           );
    }

    @Test
    @WithMockUser
    void shouldThrowMovieNotFoundExceptionWhenMovieDoesNotExistForAddRating() throws Exception {
        doThrow(MovieNotFoundException.class).when(movieService)
                                             .addRating(any(), any());

        mvc.perform(post("/movies/{movieId}/ratings", MOVIE_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "rating": 8
                        }
                        """))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser
    void shouldGetUserRating() throws Exception {
        Double rating = 8d;

        when(movieService.getRating(MOVIE_ID)).thenReturn(rating);

        mvc.perform(get("/movies/{movieId}/ratings/me", MOVIE_ID))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.rating").value(rating)
           );
    }

    @Test
    void shouldNotGetUserRatingWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(get("/movies/{movieId}/ratings/me", MOVIE_ID))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void shouldNotGetUserRatingWhenUserHasNoUserOrAdminRole() throws Exception {
        mvc.perform(get("/movies/{movieId}/ratings/me", MOVIE_ID))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldThrowMovieNotFoundExceptionForUserRatingRetrieval() throws Exception {
        when(movieService.getRating(MOVIE_ID)).thenThrow(MovieNotFoundException.class);

        mvc.perform(get("/movies/{movieId}/ratings/me", MOVIE_ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser
    void shouldAddComment() throws Exception {
        mvc.perform(post("/movies/{movieId}/comments", MOVIE_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "text": "comment"
                        }
                        """))
           .andExpect(status().isCreated());
    }

    @Test
    void shouldNotAddCommentWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/movies/{movieId}/comments", MOVIE_ID))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void shouldNotAddCommentWhenUserHasNoUserOrAdminRole() throws Exception {
        mvc.perform(post("/movies/{movieId}/comments", MOVIE_ID))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldThrowMovieNotFoundExceptionWhenMovieDoesNotExist() throws Exception {
        doThrow(MovieNotFoundException.class).when(movieService)
                                             .addComment(MOVIE_ID, "comment");

        mvc.perform(post("/movies/{movieId}/comments", MOVIE_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "text": "comment"
                        }
                        """))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldGetComments() throws Exception {
        User user = createUser("James");
        Comment comment = createComment();
        comment.setId(COMMENT_ID);
        comment.setUser(user);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Comment_.CREATED_AT)
                                                      .descending());
        Page<Comment> comments = new PageImpl<>(List.of(comment), pageable, 1L);

        when(movieService.getComments(MOVIE_ID, 0, 5, "createdAt.desc")).thenReturn(comments);

        mvc.perform(get("/movies/{movieId}/comments", MOVIE_ID)
               .param("page", "0")
               .param("limit", "5")
               .param("sort", "createdAt.desc"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.content").exists(),
               jsonPath("$.content[0].id").value(COMMENT_ID.toString()),
               jsonPath("$.content[0].author").exists(),
               jsonPath("$.content[0].author.name").value(user.getName()),
               jsonPath("$.content[0].author.pictureUrl").value(user.getPictureUrl()),
               jsonPath("$.content[0].createdAt").value(comment.getCreatedAt()),
               jsonPath("$.content[0].text").value(comment.getText()),
               jsonPath("$.content[0].isDeleted").value(comment.getIsDeleted()),
               jsonPath("$.content[0].likes").value(comment.getLikes()),
               jsonPath("$.content[0].dislikes").value(comment.getDislikes()),
               jsonPath("$.content[0].createdAt").value(comment.getCreatedAt()),
               jsonPath("$.content[0].userReaction").exists(),
               jsonPath("$.content[0].isAuthor").exists()
           );
    }

    @Test
    void shouldThrowMovieNotFoundExceptionWhenMovieDoesNotExistForCommentsRetrieval() throws Exception {
        when(movieService.getComments(MOVIE_ID, 0, 5, "createdAt.desc")).thenThrow(MovieNotFoundException.class);

        mvc.perform(get("/movies/{movieId}/comments", MOVIE_ID)
               .param("page", "0")
               .param("limit", "5")
               .param("sort", "createdAt.desc"))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    private static Movie createMoviePreview() {
        Genre horror = createGenre(GENRE_ID, "Horror");
        Genre action = createGenre(GENRE_ID, "Action");

        return Movie.builder()
                    .id(MOVIE_ID)
                    .name("The Conjuring")
                    .releaseDate(LocalDate.of(2024, 7, 29))
                    .duration(112)
                    .posterUrl("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg")
                    .genres(List.of(horror, action))
                    .build();
    }

    private static Movie createMovieWithAllDetails() {
        Director director = createDirector(DIRECTOR_ID, "Patrick Wilson");

        Movie movie = Movie.builder()
                           .id(MOVIE_ID)
                           .name("The Conjuring")
                           .filename("placeholder.mp4")
                           .releaseDate(LocalDate.of(2024, 7, 29))
                           .duration(112)
                           .description("Paranormal movie.")
                           .posterUrl("https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg")
                           .trailerUrl("https://www.youtube.com/watch?v=k10ETZ41q5o")
                           .director(director)
                           .build();

        Actor actor = createActor(ACTOR_ID, "James One");
        MovieCast cast = createMovieCast(movie, actor, "Investigator");
        ProductionCompany company = createProductionCompany(COMPANY_ID, "A24");
        Genre genre = createGenre(GENRE_ID, "Horror");
        Country country = createCountry(COUNTRY_ID, "Italy");

        movie.setCast(List.of(cast));
        movie.setProduction(List.of(company));
        movie.setGenres(List.of(genre));
        movie.setCountries(List.of(country));

        return movie;
    }

}