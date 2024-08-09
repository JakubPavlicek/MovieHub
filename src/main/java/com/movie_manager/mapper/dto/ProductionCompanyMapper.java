package com.movie_manager.mapper.dto;

import com.movie_manager.dto.ProductionCompanyDetailsResponse;
import com.movie_manager.dto.ProductionCompanyPage;
import com.movie_manager.entity.ProductionCompany;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductionCompanyMapper {

    private ProductionCompanyMapper() {
    }

    public static Set<ProductionCompany> mapToProductionCompanies(List<String> productionCompanies) {
        return productionCompanies.stream()
                                  .map(companyName -> ProductionCompany.builder()
                                                                       .name(companyName)
                                                                       .build())
                                  .collect(Collectors.toSet());
    }

    public static List<ProductionCompanyDetailsResponse> mapToProductionCompanyDetailsResponseList(Set<ProductionCompany> productionCompanies) {
        return productionCompanies.stream()
                                  .map(ProductionCompanyMapper::mapToProductionCompanyDetailsResponse)
                                  .toList();
    }

    public static ProductionCompanyDetailsResponse mapToProductionCompanyDetailsResponse(ProductionCompany company) {
        return ProductionCompanyDetailsResponse.builder()
                                               .companyId(company.getCompanyId())
                                               .name(company.getName())
                                               .build();
    }

    public static List<String> mapToProductionCompanyNames(Set<ProductionCompany> production) {
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
