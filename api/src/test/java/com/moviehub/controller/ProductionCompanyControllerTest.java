package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.exception.ProductionCompanyAlreadyExistsException;
import com.moviehub.service.MovieService;
import com.moviehub.service.ProductionCompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.moviehub.EntityBuilder.createProductionCompany;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    // TODO: add tests (then copy the tests and paste them into /genres, /countries, ...)

}