package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.Country;
import com.moviehub.entity.Movie;
import com.moviehub.exception.CountryAlreadyExistsException;
import com.moviehub.exception.CountryNotFoundException;
import com.moviehub.service.CountryService;
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

import static com.moviehub.EntityBuilder.createCountry;
import static com.moviehub.EntityBuilder.createGenres;
import static com.moviehub.EntityBuilder.createMovie;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
@Import(SecurityConfig.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CountryService countryService;

    @MockBean
    private MovieService movieService;

    private static final String NAME = "USA";
    private static final UUID ID = UUID.fromString("fcd11167-74db-4e60-bf9f-4bd8d5196014");

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddCountry() throws Exception {
        Country country = createCountry(NAME);
        country.setId(ID);

        when(countryService.addCountry(NAME)).thenReturn(country);

        mvc.perform(post("/countries")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "USA"
                        }
                        """))
           .andExpectAll(
               status().isCreated(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME)
           );
    }

    @Test
    void shouldNotAddCountryWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/countries")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "USA"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldNotAddCountryWhenUserIsNotAdmin() throws Exception {
        mvc.perform(post("/countries")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "USA"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotAddCountryWhenCountryNameIsInvalid() throws Exception {
        mvc.perform(post("/countries")
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
    void shouldThrowCountryAlreadyExistsExceptionWhenCountryNameAlreadyExists() throws Exception {
        when(countryService.addCountry(NAME)).thenThrow(CountryAlreadyExistsException.class);

        mvc.perform(post("/countries")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "USA"
                        }
                        """))
           .andExpectAll(
               status().isConflict(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldGetCountries() throws Exception {
        Country country = createCountry(NAME);
        country.setId(ID);

        when(countryService.getCountries()).thenReturn(List.of(country));

        mvc.perform(get("/countries"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.countries").exists(),
               jsonPath("$.countries[0].id").value(ID.toString()),
               jsonPath("$.countries[0].name").value(NAME)
           );
    }

    @Test
    void shouldGetCountry() throws Exception {
        Country country = createCountry(NAME);
        country.setId(ID);

        when(countryService.getCountry(ID)).thenReturn(country);

        mvc.perform(get("/countries/{countryId}", ID))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME)
           );
    }

    @Test
    void shouldThrowCountryNotFoundWhenCountryDoesNotExist() throws Exception {
        when(countryService.getCountry(ID)).thenThrow(CountryNotFoundException.class);

        mvc.perform(get("/countries/{countryId}", ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCountry() throws Exception {
        Country country = createCountry(NAME);
        country.setId(ID);

        when(countryService.updateCountry(ID, "Name")).thenReturn(country);

        mvc.perform(put("/countries/{countryId}", ID)
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
    void shouldNotUpdateCountryWhenUserHasNoAdminRole() throws Exception {
        mvc.perform(put("/countries/{countryId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Name"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotUpdateCountryWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(put("/countries/{countryId}", ID)
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
    void shouldThrowCountryNotFoundWhenCountryDoesNotExistForUpdate() throws Exception {
        Country country = createCountry(NAME);
        country.setId(ID);

        when(countryService.updateCountry(ID, "Name")).thenThrow(CountryNotFoundException.class);

        mvc.perform(put("/countries/{countryId}", ID)
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
    void shouldGetMoviesWithCountry() throws Exception {
        String movieId = "b36e7821-0410-4b2d-b36c-405a1e31028f";

        Movie movie = createMovie("Movie");
        movie.setId(UUID.fromString(movieId));
        movie.setGenres(createGenres());

        Page<Movie> movies = new PageImpl<>(List.of(movie), PageRequest.of(0, 10), 1L);

        when(movieService.getMoviesWithCountry(ID, 0, 10)).thenReturn(movies);

        mvc.perform(get("/countries/{countryId}/movies", ID)
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
    void shoulNotGetMoviesWithCountryWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/countries/{countryId}/movies", ID)
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
    void shouldThrowCountryNotFoundWhenCountryDoesNotExistForMovies() throws Exception {
        when(movieService.getMoviesWithCountry(ID, 0, 10)).thenThrow(CountryNotFoundException.class);

        mvc.perform(get("/countries/{countryId}/movies", ID)
               .param("page", "0")
               .param("limit", "10"))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

}