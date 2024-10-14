package com.moviehub.service;

import com.moviehub.entity.ProductionCompany;
import com.moviehub.exception.ProductionCompanyAlreadyExistsException;
import com.moviehub.exception.ProductionCompanyNotFoundException;
import com.moviehub.repository.ProductionCompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionCompanyServiceTest {

    @Mock
    private ProductionCompanyRepository companyRepository;

    @InjectMocks
    private ProductionCompanyService companyService;

    private static final String COMPANY_NAME = "Marvel Studios";
    private static final String UPDATED_COMPANY_NAME = "Marvel Entertainment";
    private static final UUID COMPANY_ID = UUID.fromString("7401f438-2f47-412a-9030-9b8185468cc2");
    private static final int PAGE = 0;
    private static final int LIMIT = 10;

    @Test
    void shouldAddNewProductionCompany() {
        ProductionCompany company = createProductionCompany();

        when(companyRepository.existsByName(COMPANY_NAME)).thenReturn(false);
        when(companyRepository.save(any(ProductionCompany.class))).thenReturn(company);

        ProductionCompany savedCompany = companyService.addProductionCompany(COMPANY_NAME);

        assertThat(savedCompany).isEqualTo(company);
        verify(companyRepository, times(1)).save(any(ProductionCompany.class));
    }

    @Test
    void shouldThrowExceptionWhenCompanyAlreadyExists() {
        when(companyRepository.existsByName(COMPANY_NAME)).thenReturn(true);

        assertThatThrownBy(() -> companyService.addProductionCompany(COMPANY_NAME))
            .isInstanceOf(ProductionCompanyAlreadyExistsException.class);
    }

    @Test
    void shouldGetProductionCompanyById() {
        ProductionCompany company = createProductionCompany();

        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(company));

        ProductionCompany result = companyService.getProductionCompany(COMPANY_ID);

        assertThat(result).isEqualTo(company);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFoundById() {
        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.getProductionCompany(COMPANY_ID))
            .isInstanceOf(ProductionCompanyNotFoundException.class);
    }

    @Test
    void shouldUpdateProductionCompany() {
        ProductionCompany company = createProductionCompany();

        when(companyRepository.findById(COMPANY_ID)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(ProductionCompany.class))).thenReturn(company);

        ProductionCompany updatedCompany = companyService.updateProductionCompany(COMPANY_ID, UPDATED_COMPANY_NAME);

        assertThat(updatedCompany.getName()).isEqualTo(UPDATED_COMPANY_NAME);
    }

    @Test
    void shouldGetProductionCompaniesWithPagination() {
        ProductionCompany company = createProductionCompany();
        Pageable pageable = PageRequest.of(PAGE, LIMIT, Sort.by("name")
                                                            .ascending());
        Page<ProductionCompany> companyPage = new PageImpl<>(Collections.singletonList(company));
        when(companyRepository.findAll(pageable)).thenReturn(companyPage);

        Page<ProductionCompany> result = companyService.getProductionCompanies(PAGE, LIMIT, "");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()
                         .getFirst()).isEqualTo(company);
    }

    @Test
    void shouldGetProductionCompaniesByNameWithPagination() {
        ProductionCompany company = createProductionCompany();
        Pageable pageable = PageRequest.of(PAGE, LIMIT, Sort.by("name")
                                                            .ascending());
        Page<ProductionCompany> companyPage = new PageImpl<>(Collections.singletonList(company));

        when(companyRepository.findAllByName(COMPANY_NAME, pageable)).thenReturn(companyPage);

        Page<ProductionCompany> result = companyService.getProductionCompanies(PAGE, LIMIT, COMPANY_NAME);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()
                         .getFirst()
                         .getName()).isEqualTo(COMPANY_NAME);
    }

    @Test
    void shouldGetSavedProductions() {
        ProductionCompany company = createProductionCompany();
        List<ProductionCompany> companies = List.of(company);

        when(companyRepository.findByName(COMPANY_NAME)).thenReturn(Optional.of(company));

        List<ProductionCompany> result = companyService.getSavedProductions(companies);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(company);
    }

    private ProductionCompany createProductionCompany() {
        return ProductionCompany.builder()
                                .id(COMPANY_ID)
                                .name(COMPANY_NAME)
                                .build();
    }

}