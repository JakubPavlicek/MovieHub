package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.Genre;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.exception.GenreAlreadyExistsException;
import com.moviehub.exception.GenreNotFoundException;
import com.moviehub.service.GenreService;
import com.moviehub.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createGenre;
import static com.moviehub.EntityBuilder.createGenres;
import static com.moviehub.EntityBuilder.createMovie;
import static com.moviehub.EntityBuilder.createProductionCompany;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@Import(SecurityConfig.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private MovieService movieService;

    private static final String NAME = "Action";
    private static final UUID ID = UUID.fromString("fcd11167-74db-4e60-bf9f-4bd8d5196014");

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddGenre() throws Exception {
        Genre genre = createGenre(NAME);
        genre.setId(ID);

        when(genreService.addGenre(NAME)).thenReturn(genre);

        mvc.perform(post("/genres")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Action"
                        }
                        """))
           .andExpectAll(
               status().isCreated(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME)
           );
    }

    @Test
    void shouldNotAddGenreWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/genres")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Action"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldNotAddGenreWhenUserIsNotAdmin() throws Exception {
        mvc.perform(post("/genres")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Action"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotAddGenreWhenGenreNameIsInvalid() throws Exception {
        mvc.perform(post("/genres")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": ""
                        }
                        """))
           .andExpectAll(
               status().isBadRequest(),
               jsonPath("$.contextInfo.name").exists()
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldThrowGenreAlreadyExistsExceptionWhenGenreNameAlreadyExists() throws Exception {
        when(genreService.addGenre(NAME)).thenThrow(GenreAlreadyExistsException.class);

        mvc.perform(post("/genres")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Action"
                        }
                        """))
           .andExpectAll(
               status().isConflict(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldGetGenres() throws Exception {
        Genre genre = createGenre(NAME);
        genre.setId(ID);

        when(genreService.getGenres()).thenReturn(List.of(genre));

        mvc.perform(get("/genres"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.genres").exists(),
               jsonPath("$.genres[0].id").value(ID.toString()),
               jsonPath("$.genres[0].name").value(NAME)
           );
    }

    @Test
    void shouldGetGenre() throws Exception {
        Genre genre = createGenre(NAME);
        genre.setId(ID);

        when(genreService.getGenre(ID)).thenReturn(genre);

        mvc.perform(get("/genres/{genreId}", ID))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME)
           );
    }

    @Test
    void shouldThrowGenreNotFoundWhenGenreDoesNotExist() throws Exception {
        when(genreService.getGenre(ID)).thenThrow(GenreNotFoundException.class);

        mvc.perform(get("/genres/{genreId}", ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateGenre() throws Exception {
        Genre genre = createGenre(NAME);
        genre.setId(ID);

        when(genreService.updateGenre(ID, "Name")).thenReturn(genre);

        mvc.perform(put("/genres/{genreId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Name"
                        }
                        """))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME)
           );
    }

    @Test
    @WithMockUser
    void shouldNotUpdateGenreWhenUserHasNoAdminRole() throws Exception {
        mvc.perform(put("/genres/{genreId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Name"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotUpdateGenreWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(put("/genres/{genreId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Name"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldThrowGenreNotFoundWhenGenreDoesNotExistForUpdate() throws Exception {
        ProductionCompany company = createProductionCompany("Name");
        company.setId(ID);

        when(genreService.updateGenre(ID, "Name")).thenThrow(GenreNotFoundException.class);

        mvc.perform(put("/genres/{genreId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Name"
                        }
                        """))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldGetMoviesWithGenre() throws Exception {
        String movieId = "b36e7821-0410-4b2d-b36c-405a1e31028f";

        Movie movie = createMovie("Movie");
        movie.setId(UUID.fromString(movieId));
        movie.setGenres(createGenres());

        Page<Movie> movies = new PageImpl<>(List.of(movie), PageRequest.of(0, 10), 1L);

        when(movieService.getMoviesWithGenre(ID, 0, 10)).thenReturn(movies);

        mvc.perform(get("/genres/{genreId}/movies", ID)
               .param("page", "0")
               .param("limit", "10"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.content[0].id").value(movieId),
               jsonPath("$.content[0].name").value("Movie")
           );
    }

    @Test
    void shoulNotGetMoviesWithGenreWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/genres/{genreId}/movies", ID)
               .param("page", "-1")
               .param("limit", "-1"))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.page").exists(),
               jsonPath("$.contextInfo.limit").exists()
           );
    }

    @Test
    void shouldThrowGenreNotFoundWhenGenreDoesNotExistForMovies() throws Exception {
        when(movieService.getMoviesWithGenre(ID, 0, 10)).thenThrow(GenreNotFoundException.class);

        mvc.perform(get("/genres/{genreId}/movies", ID)
               .param("page", "0")
               .param("limit", "10"))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

}