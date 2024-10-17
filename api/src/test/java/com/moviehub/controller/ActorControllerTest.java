package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.Actor;
import com.moviehub.entity.Gender;
import com.moviehub.entity.Movie;
import com.moviehub.exception.ActorAlreadyExistsException;
import com.moviehub.exception.ActorNotFoundException;
import com.moviehub.service.ActorService;
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

import static com.moviehub.EntityBuilder.createActor;
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

@WebMvcTest(ActorController.class)
@Import(SecurityConfig.class)
class ActorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ActorService actorService;

    @MockBean
    private MovieService movieService;

    private static final String NAME = "James";
    private static final String BIO = "bio";
    private static final UUID ID = UUID.fromString("fcd11167-74db-4e60-bf9f-4bd8d5196014");

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddActor() throws Exception {
        Actor actor = Actor.builder()
                           .id(ID)
                           .name(NAME)
                           .bio(BIO)
                           .gender(Gender.MALE)
                           .build();

        when(actorService.addActor(any())).thenReturn(actor);

        mvc.perform(post("/actors")
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
    void shouldNotAddActorWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/actors")
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
    void shouldNotAddActorWhenUserIsNotAdmin() throws Exception {
        mvc.perform(post("/actors")
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
    void shouldNotAddActorWhenActorNameIsInvalid() throws Exception {
        mvc.perform(post("/actors")
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
    void shouldThrowActorAlreadyExistsExceptionWhenActorNameAlreadyExists() throws Exception {
        when(actorService.addActor(any())).thenThrow(ActorAlreadyExistsException.class);

        mvc.perform(post("/actors")
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
    void shouldGetActors() throws Exception {
        Actor actor = createActor(NAME);
        actor.setId(ID);
        Page<Actor> actors = new PageImpl<>(List.of(actor), PageRequest.of(0, 10), 1L);

        when(actorService.getActors(0, 10, NAME)).thenReturn(actors);

        mvc.perform(get("/actors")
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
    void shouldNotGetActorsWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/actors")
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
    void shouldGetActor() throws Exception {
        Actor actor = Actor.builder()
                           .id(ID)
                           .name(NAME)
                           .bio(BIO)
                           .gender(Gender.MALE)
                           .build();

        when(actorService.getActor(ID)).thenReturn(actor);

        mvc.perform(get("/actors/{actorId}", ID))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME),
               jsonPath("$.bio").value(BIO),
               jsonPath("$.gender").value(Gender.MALE.getValue())
           );
    }

    @Test
    void shouldThrowActorNotFoundWhenActorDoesNotExist() throws Exception {
        when(actorService.getActor(ID)).thenThrow(ActorNotFoundException.class);

        mvc.perform(get("/actors/{actorId}", ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateActor() throws Exception {
        Actor actor = Actor.builder()
                           .id(ID)
                           .name(NAME)
                           .bio(BIO)
                           .gender(Gender.MALE)
                           .build();

        when(actorService.updateActor(any(), any())).thenReturn(actor);

        mvc.perform(put("/actors/{actorId}", ID)
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
    void shouldNotUpdateActorWhenUserHasNoAdminRole() throws Exception {
        mvc.perform(put("/actors/{actorId}", ID)
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
        mvc.perform(put("/actors/{actorId}", ID)
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
    void shouldThrowActorNotFoundWhenActorDoesNotExistForUpdate() throws Exception {
        when(actorService.updateActor(any(), any())).thenThrow(ActorNotFoundException.class);

        mvc.perform(put("/actors/{actorId}", ID)
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
    void shouldGetMoviesWithActor() throws Exception {
        String movieId = "b36e7821-0410-4b2d-b36c-405a1e31028f";

        Movie movie = createMovie("Movie");
        movie.setId(UUID.fromString(movieId));
        movie.setGenres(createGenres());

        Page<Movie> movies = new PageImpl<>(List.of(movie), PageRequest.of(0, 10), 1L);

        when(movieService.getMoviesWithActor(ID, 0, 10)).thenReturn(movies);

        mvc.perform(get("/actors/{actorId}/movies", ID)
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
    void shoulNotGetMoviesWithActorWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/actors/{actorId}/movies", ID)
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
    void shouldThrowActorNotFoundWhenActorDoesNotExistForMovies() throws Exception {
        when(movieService.getMoviesWithActor(ID, 0, 10)).thenThrow(ActorNotFoundException.class);

        mvc.perform(get("/actors/{actorId}/movies", ID)
               .param("page", "0")
               .param("limit", "10"))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

}