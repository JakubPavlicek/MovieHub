package com.movie_manager.mapper.dto;

import com.movie_manager.dto.ProductionCompanyDTO;
import com.movie_manager.entity.ProductionCompany;

import java.util.List;

public class ProductionCompanyMapper {

    private ProductionCompanyMapper() {
    }

    public static List<ProductionCompany> mapToProductionCompanies(List<String> productionCompanies) {
        return productionCompanies.stream()
                                  .map(companyName -> ProductionCompany.builder().name(companyName).build())
                                  .toList();
    }

    public static List<ProductionCompanyDTO> mapToProductionCompanyDTOS(List<ProductionCompany> productionCompanies) {
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

}
