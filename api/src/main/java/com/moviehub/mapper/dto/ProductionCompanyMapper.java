package com.moviehub.mapper.dto;

import com.moviehub.dto.ProductionCompanyDetailsResponse;
import com.moviehub.dto.ProductionCompanyPage;
import com.moviehub.entity.ProductionCompany;
import org.springframework.data.domain.Page;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between ProductionCompany entities and Data Transfer Objects (DTOs).
public class ProductionCompanyMapper {

    // Private constructor to prevent instantiation.
    private ProductionCompanyMapper() {
    }

    /// Maps a list of production company names to ProductionCompany entities.
    ///
    /// @param productionCompanies The list of production company names.
    /// @return A list of ProductionCompany entities.
    public static List<ProductionCompany> mapToProductionCompanies(List<String> productionCompanies) {
        return productionCompanies.stream()
                                  .map(companyName -> ProductionCompany.builder()
                                                                       .name(companyName)
                                                                       .build())
                                  .toList();
    }

    /// Maps a list of ProductionCompany entities to ProductionCompanyDetailsResponse DTOs.
    ///
    /// @param productionCompanies The list of ProductionCompany entities.
    /// @return A list of ProductionCompanyDetailsResponse DTOs.
    public static List<ProductionCompanyDetailsResponse> mapToProductionCompanyDetailsResponseList(List<ProductionCompany> productionCompanies) {
        return productionCompanies.stream()
                                  .map(ProductionCompanyMapper::mapToProductionCompanyDetailsResponse)
                                  .toList();
    }

    /// Maps a ProductionCompany entity to a ProductionCompanyDetailsResponse DTO.
    ///
    /// @param company The ProductionCompany entity to map.
    /// @return A ProductionCompanyDetailsResponse DTO.
    public static ProductionCompanyDetailsResponse mapToProductionCompanyDetailsResponse(ProductionCompany company) {
        return ProductionCompanyDetailsResponse.builder()
                                               .id(company.getId())
                                               .name(company.getName())
                                               .build();
    }

    /// Maps a list of ProductionCompany entities to their names.
    ///
    /// @param production The list of ProductionCompany entities.
    /// @return A list of production company names.
    public static List<String> mapToProductionCompanyNames(List<ProductionCompany> production) {
        return production.stream()
                         .map(ProductionCompany::getName)
                         .toList();
    }

    /// Maps a Page of ProductionCompany entities to a ProductionCompanyPage DTO.
    ///
    /// @param companies The Page of ProductionCompany entities.
    /// @return A ProductionCompanyPage DTO.
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

    // Private method to map a Page of ProductionCompany entities to a list of ProductionCompanyDetailsResponse DTOs.
    private static List<ProductionCompanyDetailsResponse> mapToProductionCompanyDetailsResponseList(Page<ProductionCompany> companies) {
        return companies.stream()
                        .map(ProductionCompanyMapper::mapToProductionCompanyDetailsResponse)
                        .toList();
    }

}
