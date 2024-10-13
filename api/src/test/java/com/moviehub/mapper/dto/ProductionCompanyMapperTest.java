package com.moviehub.mapper.dto;

import com.moviehub.dto.ProductionCompanyDetailsResponse;
import com.moviehub.dto.ProductionCompanyPage;
import com.moviehub.entity.ProductionCompany;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductionCompanyMapperTest {

    private static final UUID COMPANY_ID = UUID.fromString("10013b40-df6f-4892-a222-8b48aa77d4ee");
    private static final String COMPANY_A = "Company A";
    private static final String COMPANY_B = "Company B";

    @Test
    void shouldMapToProductionCompanies() {
        List<String> companyNames = List.of(COMPANY_A, COMPANY_B);

        List<ProductionCompany> productionCompanies = ProductionCompanyMapper.mapToProductionCompanies(companyNames);

        assertThat(productionCompanies).hasSize(2);
        assertThat(productionCompanies.get(0).getName()).isEqualTo(COMPANY_A);
        assertThat(productionCompanies.get(1).getName()).isEqualTo(COMPANY_B);
    }

    @Test
    void shouldMapToProductionCompanyDetailsResponseList() {
        List<ProductionCompany> productionCompanies = List.of(
            buildProductionCompany(UUID.randomUUID(), COMPANY_A),
            buildProductionCompany(UUID.randomUUID(), COMPANY_B)
        );

        List<ProductionCompanyDetailsResponse> responseList = ProductionCompanyMapper.mapToProductionCompanyDetailsResponseList(productionCompanies);

        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getId()).isEqualTo(productionCompanies.get(0).getId());
        assertThat(responseList.get(0).getName()).isEqualTo(COMPANY_A);
        assertThat(responseList.get(1).getId()).isEqualTo(productionCompanies.get(1).getId());
        assertThat(responseList.get(1).getName()).isEqualTo(COMPANY_B);
    }

    @Test
    void shouldMapToProductionCompanyDetailsResponse() {
        ProductionCompany company = buildProductionCompany(COMPANY_ID, COMPANY_A);

        ProductionCompanyDetailsResponse response = ProductionCompanyMapper.mapToProductionCompanyDetailsResponse(company);

        assertThat(response.getId()).isEqualTo(COMPANY_ID);
        assertThat(response.getName()).isEqualTo(COMPANY_A);
    }

    @Test
    void shouldMapToProductionCompanyNames() {
        List<ProductionCompany> productionCompanies = List.of(
            buildProductionCompany(UUID.randomUUID(), COMPANY_A),
            buildProductionCompany(UUID.randomUUID(), COMPANY_B)
        );

        List<String> companyNames = ProductionCompanyMapper.mapToProductionCompanyNames(productionCompanies);

        assertThat(companyNames).containsExactly(COMPANY_A, COMPANY_B);
    }

    @Test
    void shouldMapToProductionCompanyPage() {
        List<ProductionCompany> companies = List.of(
            buildProductionCompany(UUID.randomUUID(), COMPANY_A),
            buildProductionCompany(UUID.randomUUID(), COMPANY_B)
        );
        Page<ProductionCompany> companyPage = new PageImpl<>(companies, PageRequest.of(0, 10), companies.size());

        ProductionCompanyPage productionCompanyPage = ProductionCompanyMapper.mapToProductionCompanyPage(companyPage);

        assertThat(productionCompanyPage.getContent()).hasSize(2);
        assertThat(productionCompanyPage.getTotalElements()).isEqualTo(2);
        assertThat(productionCompanyPage.getTotalPages()).isEqualTo(1);
        assertThat(productionCompanyPage.getNumber()).isZero();
        assertThat(productionCompanyPage.getFirst()).isTrue();
        assertThat(productionCompanyPage.getLast()).isTrue();
    }

    private ProductionCompany buildProductionCompany(UUID id, String name) {
        return ProductionCompany.builder()
                                .id(id)
                                .name(name)
                                .build();
    }

}