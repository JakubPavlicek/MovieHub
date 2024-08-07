package com.movie_manager.service;

import com.movie_manager.entity.ProductionCompany;
import com.movie_manager.exception.ProductionCompanyAlreadyExistsException;
import com.movie_manager.exception.ProductionCompanyNotFoundException;
import com.movie_manager.repository.ProductionCompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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

    public ProductionCompany addProductionCompany(String name) {
        if (companyRepository.existsByName(name)) {
            throw new ProductionCompanyAlreadyExistsException("Production company with name " + name + " already exists");
        }

        ProductionCompany productionCompany = new ProductionCompany();
        productionCompany.setName(name);

        return companyRepository.save(productionCompany);
    }

    public List<ProductionCompany> getProductionCompanies() {
        return companyRepository.findAll();
    }

    public ProductionCompany getProductionCompany(String companyId) {
        return companyRepository.findById(companyId)
                                .orElseThrow(() -> new ProductionCompanyNotFoundException("Production company with ID: " + companyId + " not found"));
    }

}
