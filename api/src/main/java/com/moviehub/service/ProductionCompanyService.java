package com.moviehub.service;

import com.moviehub.entity.ProductionCompany;
import com.moviehub.entity.ProductionCompany_;
import com.moviehub.exception.ProductionCompanyAlreadyExistsException;
import com.moviehub.exception.ProductionCompanyNotFoundException;
import com.moviehub.repository.ProductionCompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductionCompanyService {

    private final ProductionCompanyRepository companyRepository;

    public List<ProductionCompany> getSavedProductions(List<ProductionCompany> companies) {
        return companies.stream()
                        .map(this::getSavedProductionCompany)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    private ProductionCompany getSavedProductionCompany(ProductionCompany company) {
        return companyRepository.findByName(company.getName())
                                .orElseGet(() -> companyRepository.save(company));
    }

    public ProductionCompany addProductionCompany(String name) {
        if (companyRepository.existsByName(name)) {
            throw new ProductionCompanyAlreadyExistsException("Production company with name: " + name + " already exists");
        }

        ProductionCompany productionCompany = new ProductionCompany();
        productionCompany.setName(name);

        return companyRepository.save(productionCompany);
    }

    public Page<ProductionCompany> getProductionCompanies(Integer page, Integer limit, String name) {
        Sort sort = Sort.by(ProductionCompany_.NAME).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return companyRepository.findAll(pageable);
        }

        return companyRepository.findAllByName(name, pageable);
    }

    public ProductionCompany getProductionCompany(UUID companyId) {
        return companyRepository.findById(companyId)
                                .orElseThrow(() -> new ProductionCompanyNotFoundException("Production company with ID: " + companyId + " not found"));
    }

    public ProductionCompany updateProductionCompany(UUID companyId, String name) {
        ProductionCompany company = getProductionCompany(companyId);
        company.setName(name);

        return companyRepository.save(company);
    }

}
