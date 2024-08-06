package com.movie_manager.mapper.dto;

import com.movie_manager.dto.ProductionCompanyDTO;
import com.movie_manager.entity.ProductionCompany;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductionCompanyMapper {

    private ProductionCompanyMapper() {
    }

    public static Set<ProductionCompany> mapToProductionCompanies(List<String> productionCompanies) {
        return productionCompanies.stream()
                                  .map(companyName -> ProductionCompany.builder().name(companyName).build())
                                  .collect(Collectors.toSet());
    }

    public static List<ProductionCompanyDTO> mapToProductionCompanyDTOS(Set<ProductionCompany> productionCompanies) {
        return productionCompanies.stream()
                                  .map(ProductionCompanyMapper::mapToProductionCompanyDTO)
                                  .toList();
    }

    private static ProductionCompanyDTO mapToProductionCompanyDTO(ProductionCompany company) {
        return ProductionCompanyDTO.builder()
                                   .companyId(company.getCompanyId())
                                   .name(company.getName())
                                   .build();
    }

    public static List<String> mapToProductionCompanyNames(Set<ProductionCompany> production) {
        return production.stream()
                         .map(ProductionCompany::getName)
                         .toList();
    }

}
