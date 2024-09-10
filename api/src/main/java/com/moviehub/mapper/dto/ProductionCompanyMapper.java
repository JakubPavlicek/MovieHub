package com.moviehub.mapper.dto;

import com.moviehub.dto.ProductionCompanyDetailsResponse;
import com.moviehub.dto.ProductionCompanyPage;
import com.moviehub.entity.ProductionCompany;
import org.springframework.data.domain.Page;

import java.util.List;

public class ProductionCompanyMapper {

    private ProductionCompanyMapper() {
    }

    public static List<ProductionCompany> mapToProductionCompanies(List<String> productionCompanies) {
        return productionCompanies.stream()
                                  .map(companyName -> ProductionCompany.builder()
                                                                       .name(companyName)
                                                                       .build())
                                  .toList();
    }

    public static List<ProductionCompanyDetailsResponse> mapToProductionCompanyDetailsResponseList(List<ProductionCompany> productionCompanies) {
        return productionCompanies.stream()
                                  .map(ProductionCompanyMapper::mapToProductionCompanyDetailsResponse)
                                  .toList();
    }

    public static ProductionCompanyDetailsResponse mapToProductionCompanyDetailsResponse(ProductionCompany company) {
        return ProductionCompanyDetailsResponse.builder()
                                               .id(company.getId())
                                               .name(company.getName())
                                               .build();
    }

    public static List<String> mapToProductionCompanyNames(List<ProductionCompany> production) {
        return production.stream()
                         .map(ProductionCompany::getName)
                         .toList();
    }

    public static ProductionCompanyPage mapToProductionCompanyPage(Page<ProductionCompany> companies) {
        return ProductionCompanyPage.builder()
                                    .content(mapToProductionCompanyDetailsResponseList(companies))
                                    .pageable(PageableMapper.mapToPageableDTO(companies.getPageable()))
                                    .last(companies.isLast())
                                    .totalElements(companies.getTotalElements())
                                    .totalPages(companies.getTotalPages())
                                    .first(companies.isFirst())
                                    .size(companies.getSize())
                                    .number(companies.getNumber())
                                    .sort(SortMapper.mapToSortDTO(companies.getSort()))
                                    .numberOfElements(companies.getNumberOfElements())
                                    .empty(companies.isEmpty())
                                    .build();
    }

    private static List<ProductionCompanyDetailsResponse> mapToProductionCompanyDetailsResponseList(Page<ProductionCompany> companies) {
        return companies.stream()
                        .map(ProductionCompanyMapper::mapToProductionCompanyDetailsResponse)
                        .toList();
    }

}
