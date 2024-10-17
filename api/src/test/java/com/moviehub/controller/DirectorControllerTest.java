package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.Director;
import com.moviehub.entity.Gender;
import com.moviehub.entity.Movie;
import com.moviehub.exception.DirectorAlreadyExistsException;
import com.moviehub.exception.DirectorNotFoundException;
import com.moviehub.service.DirectorService;
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

import static com.moviehub.EntityBuilder.createDirector;
import static com.moviehub.EntityBuilder.createGenres;
import static com.moviehub.EntityBuilder.createMovie;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DirectorController.class)
@Import(SecurityConfig.class)
class DirectorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DirectorService directorService;

    @MockBean
    private MovieService movieService;

    private static final String NAME = "James";
    private static final String BIO = "bio";
    private static final UUID ID = UUID.fromString("fcd11167-74db-4e60-bf9f-4bd8d5196014");

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddDirector() throws Exception {
        Director director = Director.builder()
                                    .id(ID)
                                    .name(NAME)
                                    .bio(BIO)
                                    .gender(Gender.MALE)
                                    .build();

        when(directorService.addDirector(any())).thenReturn(director);

        mvc.perform(post("/directors")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "James",
                          "bio": "bio",
                          "gender": "Male"
                        }
                        """))
           .andExpectAll(
               status().isCreated(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME),
               jsonPath("$.bio").value(BIO),
               jsonPath("$.gender").value(Gender.MALE.getValue())
           );
    }

    @Test
    void shouldNotAddDirectorWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/directors")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "James",
                          "bio": "bio",
                          "gender": "Male"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldNotAddDirectorWhenUserIsNotAdmin() throws Exception {
        mvc.perform(post("/directors")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "James",
                          "bio": "bio",
                          "gender": "Male"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotAddDirectorWhenDirectorNameIsInvalid() throws Exception {
        mvc.perform(post("/directors")
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
    void shouldThrowDirectorAlreadyExistsExceptionWhenDirectorNameAlreadyExists() throws Exception {
        when(directorService.addDirector(any())).thenThrow(DirectorAlreadyExistsException.class);

        mvc.perform(post("/directors")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "James",
                          "bio": "bio",
                          "gender": "Male"
                        }
                        """))
           .andExpectAll(
               status().isConflict(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldGetDirectors() throws Exception {
        Director director = createDirector(NAME);
        director.setId(ID);
        Page<Director> directors = new PageImpl<>(List.of(director), PageRequest.of(0, 10), 1L);

        when(directorService.getDirectors(0, 10, NAME)).thenReturn(directors);

        mvc.perform(get("/directors")
               .param("page", "0")
               .param("limit", "10")
               .param("name", NAME))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.content").exists(),
               jsonPath("$.content[0].id").value(ID.toString()),
               jsonPath("$.content[0].name").value(NAME)
           );
    }

    @Test
    void shouldNotGetDirectorsWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/directors")
               .param("page", "-1")
               .param("limit", "-1")
               .param("name", "<script>"))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.page").exists(),
               jsonPath("$.contextInfo.limit").exists(),
               jsonPath("$.contextInfo.name").exists()
           );
    }

    @Test
    void shouldGetDirector() throws Exception {
        Director director = Director.builder()
                                    .id(ID)
                                    .name(NAME)
                                    .bio(BIO)
                                    .gender(Gender.MALE)
                                    .build();

        when(directorService.getDirector(ID)).thenReturn(director);

        mvc.perform(get("/directors/{directorId}", ID))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME),
               jsonPath("$.bio").value(BIO),
               jsonPath("$.gender").value(Gender.MALE.getValue())
           );
    }

    @Test
    void shouldThrowDirectorNotFoundWhenDirectorDoesNotExist() throws Exception {
        when(directorService.getDirector(ID)).thenThrow(DirectorNotFoundException.class);

        mvc.perform(get("/directors/{directorId}", ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateDirector() throws Exception {
        Director director = Director.builder()
                                    .id(ID)
                                    .name(NAME)
                                    .bio(BIO)
                                    .gender(Gender.MALE)
                                    .build();

        when(directorService.updateDirector(any(), any())).thenReturn(director);

        mvc.perform(put("/directors/{directorId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "James",
                          "bio": "bio",
                          "gender": "Male"
                        }
                        """))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME),
               jsonPath("$.bio").value(BIO),
               jsonPath("$.gender").value(Gender.MALE.getValue())
           );
    }

    @Test
    @WithMockUser
    void shouldNotUpdateDirectorWhenUserHasNoAdminRole() throws Exception {
        mvc.perform(put("/directors/{directorId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Name"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotUpdateActorWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(put("/directors/{directorId}", ID)
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
    void shouldThrowDirectorNotFoundWhenDirectorDoesNotExistForUpdate() throws Exception {
        when(directorService.updateDirector(any(), any())).thenThrow(DirectorNotFoundException.class);

        mvc.perform(put("/directors/{directorId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "James",
                          "bio": "bio",
                          "gender": "Male"
                        }
                        """))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldGetMoviesWithDirector() throws Exception {
        String movieId = "b36e7821-0410-4b2d-b36c-405a1e31028f";

        Movie movie = createMovie("Movie");
        movie.setId(UUID.fromString(movieId));
        movie.setGenres(createGenres());

        Page<Movie> movies = new PageImpl<>(List.of(movie), PageRequest.of(0, 10), 1L);

        when(movieService.getMoviesWithDirector(ID, 0, 10)).thenReturn(movies);

        mvc.perform(get("/directors/{directorId}/movies", ID)
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
    void shoulNotGetMoviesWithDirectorWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/directors/{directorId}/movies", ID)
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
    void shouldThrowDirectorNotFoundWhenDirectorDoesNotExistForMovies() throws Exception {
        when(movieService.getMoviesWithDirector(ID, 0, 10)).thenThrow(DirectorNotFoundException.class);

        mvc.perform(get("/directors/{directorId}/movies", ID)
               .param("page", "0")
               .param("limit", "10"))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

}