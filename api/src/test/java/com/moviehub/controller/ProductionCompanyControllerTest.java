package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.exception.ProductionCompanyAlreadyExistsException;
import com.moviehub.exception.ProductionCompanyNotFoundException;
import com.moviehub.service.MovieService;
import com.moviehub.service.ProductionCompanyService;
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

@WebMvcTest(ProductionCompanyController.class)
@Import(SecurityConfig.class)
class ProductionCompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductionCompanyService companyService;

    @MockBean
    private MovieService movieService;

    private static final String NAME = "A24";
    private static final UUID ID = UUID.fromString("fcd11167-74db-4e60-bf9f-4bd8d5196014");

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddProductionCompany() throws Exception {
        ProductionCompany company = createProductionCompany(NAME);
        company.setId(ID);

        when(companyService.addProductionCompany(NAME)).thenReturn(company);

        mvc.perform(post("/production-companies")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "A24"
                        }
                        """))
           .andExpectAll(
               status().isCreated(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME)
           );
    }

    @Test
    void shouldNotAddProductionCompanyWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/production-companies")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "A24"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldNotAddProductionCompanyWhenUserIsNotAdmin() throws Exception {
        mvc.perform(post("/production-companies")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "A24"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotAddProductionCompanyWhenCompanyNameIsInvalid() throws Exception {
        mvc.perform(post("/production-companies")
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
    void shouldThrowProductionCompanyAlreadyExistsExceptionWhenProductionNameAlreadyExists() throws Exception {
        when(companyService.addProductionCompany(NAME)).thenThrow(ProductionCompanyAlreadyExistsException.class);

        mvc.perform(post("/production-companies")
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "A24"
                        }
                        """))
           .andExpectAll(
               status().isConflict(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldGetProductionCompanies() throws Exception {
        ProductionCompany company = createProductionCompany(NAME);
        company.setId(ID);
        Page<ProductionCompany> companies = new PageImpl<>(List.of(company), PageRequest.of(0, 10), 1L);

        when(companyService.getProductionCompanies(0, 10, NAME)).thenReturn(companies);

        mvc.perform(get("/production-companies")
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
    void shouldNotGetProductionCompaniesWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/production-companies")
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
    void shouldGetProductionCompany() throws Exception {
        ProductionCompany company = createProductionCompany(NAME);
        company.setId(ID);

        when(companyService.getProductionCompany(ID)).thenReturn(company);

        mvc.perform(get("/production-companies/{companyId}", ID))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value(NAME)
           );
    }

    @Test
    void shouldThrowProductionCompanyNotFoundWhenCompanyDoesNotExist() throws Exception {
        when(companyService.getProductionCompany(ID)).thenThrow(ProductionCompanyNotFoundException.class);

        mvc.perform(get("/production-companies/{companyId}", ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProductionCompany() throws Exception {
        ProductionCompany company = createProductionCompany("New Name");
        company.setId(ID);

        when(companyService.updateProductionCompany(ID, "New Name")).thenReturn(company);

        mvc.perform(put("/production-companies/{companyId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "New Name"
                        }
                        """))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.id").value(ID.toString()),
               jsonPath("$.name").value("New Name")
           );
    }

    @Test
    @WithMockUser
    void shouldNotUpdateProductionCompanyWhenUserHasNoAdminRole() throws Exception {
        mvc.perform(put("/production-companies/{companyId}", ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "name": "Name"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotUpdateProductionCompanyWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(put("/production-companies/{companyId}", ID)
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
    void shouldThrowProductionCompanyNotFoundWhenCompanyDoesNotExistForUpdate() throws Exception {
        ProductionCompany company = createProductionCompany("Name");
        company.setId(ID);

        when(companyService.updateProductionCompany(ID, "Name")).thenThrow(ProductionCompanyNotFoundException.class);

        mvc.perform(put("/production-companies/{companyId}", ID)
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
    void shouldGetMoviesWithProductionCompany() throws Exception {
        String movieId = "b36e7821-0410-4b2d-b36c-405a1e31028f";

        Movie movie = createMovie("Movie");
        movie.setId(UUID.fromString(movieId));
        movie.setGenres(createGenres());

        Page<Movie> movies = new PageImpl<>(List.of(movie), PageRequest.of(0, 10), 1L);

        when(movieService.getMoviesWithProductionCompany(ID, 0, 10)).thenReturn(movies);

        mvc.perform(get("/production-companies/{companyId}/movies", ID)
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
    void shoulNotGetMoviesWithProductionCompanyWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/production-companies/{companyId}/movies", ID)
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
    void shouldThrowProductionCompanyNotFoundWhenCompanyDoesNotExistForMovies() throws Exception {
        when(movieService.getMoviesWithProductionCompany(ID, 0, 10)).thenThrow(ProductionCompanyNotFoundException.class);

        mvc.perform(get("/production-companies/{companyId}/movies", ID)
               .param("page", "0")
               .param("limit", "10"))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

}