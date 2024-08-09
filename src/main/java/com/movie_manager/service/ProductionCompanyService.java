package com.movie_manager.service;

import com.movie_manager.entity.ProductionCompany;
import com.movie_manager.entity.ProductionCompany_;
import com.movie_manager.exception.ProductionCompanyAlreadyExistsException;
import com.movie_manager.exception.ProductionCompanyNotFoundException;
import com.movie_manager.repository.ProductionCompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public ProductionCompany addProductionCompany(String name) {
        if (companyRepository.existsByName(name)) {
            throw new ProductionCompanyAlreadyExistsException("Production company with name " + name + " already exists");
        }

        ProductionCompany productionCompany = new ProductionCompany();
        productionCompany.setName(name);

        return companyRepository.save(productionCompany);
    }

    public Page<ProductionCompany> getProductionCompanies(Integer page, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, ProductionCompany_.NAME);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return companyRepository.findAll(pageable);
    }

    public ProductionCompany getProductionCompany(String companyId) {
        return companyRepository.findById(companyId)
                                .orElseThrow(() -> new ProductionCompanyNotFoundException("Production company with ID: " + companyId + " not found"));
    }

}
