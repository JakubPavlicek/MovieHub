package com.movie_manager.service;

import com.movie_manager.entity.ProductionCompany;
import com.movie_manager.repository.ProductionCompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionCompanyService {

    private final ProductionCompanyRepository companyRepository;

    @Transactional
    public Set<ProductionCompany> getSavedProduction(Set<ProductionCompany> companies) {
        return companies.stream()
                        .map(this::getSavedProductionCompany)
                        .collect(Collectors.toCollection(HashSet::new));
    }

    private ProductionCompany getSavedProductionCompany(ProductionCompany company) {
        return companyRepository.findByName(company.getName())
                                .orElseGet(() -> companyRepository.save(company));
    }

}
