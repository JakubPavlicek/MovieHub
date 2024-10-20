package com.moviehub.service;

import com.moviehub.entity.ProductionCompany;
import com.moviehub.entity.ProductionCompany_;
import com.moviehub.exception.ProductionCompanyAlreadyExistsException;
import com.moviehub.exception.ProductionCompanyNotFoundException;
import com.moviehub.repository.ProductionCompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing production companies in the MovieHub application.
/// Provides methods for retrieving, adding, updating, and managing production company entities.
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductionCompanyService {

    /// Repository for handling production companies in the database.
    private final ProductionCompanyRepository companyRepository;

    /// Retrieves a list of saved production companies.
    /// If a production company does not exist in the database, it will be saved.
    ///
    /// @param companies List of production companies to retrieve or save.
    /// @return List of saved production companies.
    public List<ProductionCompany> getSavedProductions(List<ProductionCompany> companies) {
        log.info("retrieving saved production companies");

        return companies.stream()
                        .map(this::getSavedProductionCompany)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    /// Retrieves a saved production company by name or saves it if it does not exist.
    ///
    /// @param company The production company to retrieve or save.
    /// @return The retrieved or saved production company.
    private ProductionCompany getSavedProductionCompany(ProductionCompany company) {
        return companyRepository.findByName(company.getName())
                                .orElseGet(() -> companyRepository.save(company));
    }

    /// Retrieves a production company by its ID.
    ///
    /// @param companyId The ID of the production company to retrieve.
    /// @return The production company with the specified ID.
    /// @throws ProductionCompanyNotFoundException if no production company with the specified ID exists.
    public ProductionCompany getProductionCompany(UUID companyId) {
        log.info("retrieving production company: {}", companyId);

        return companyRepository.findById(companyId)
                                .orElseThrow(() -> new ProductionCompanyNotFoundException("Production company with ID: " + companyId + " not found"));
    }

    /// Retrieves a paginated list of production companies, optionally filtered by name.
    ///
    /// @param page The page number to retrieve.
    /// @param limit The number of items per page.
    /// @param name The name to filter the production companies by. If empty, retrieves all.
    /// @return A paginated list of production companies.
    public Page<ProductionCompany> getProductionCompanies(Integer page, Integer limit, String name) {
        log.info("retrieving production companies with page: {}, limit: {}, name: {}", page, limit, name);

        Sort sort = Sort.by(ProductionCompany_.NAME).ascending();
        Pageable pageable = PageRequest.of(page, limit, sort);

        if (name.isEmpty()) {
            return companyRepository.findAll(pageable);
        }

        return companyRepository.findAllByName(name, pageable);
    }

    /// Adds a new production company with the specified name.
    ///
    /// @param name The name of the production company to add.
    /// @return The newly added production company.
    /// @throws ProductionCompanyAlreadyExistsException if a production company with the same name already exists.
    public ProductionCompany addProductionCompany(String name) {
        if (companyRepository.existsByName(name)) {
            throw new ProductionCompanyAlreadyExistsException("Production company with name: " + name + " already exists");
        }

        log.info("adding new production company: {}", name);

        ProductionCompany productionCompany = new ProductionCompany();
        productionCompany.setName(name);

        return companyRepository.save(productionCompany);
    }

    /// Updates an existing production company with a new name.
    ///
    /// @param companyId The ID of the production company to update.
    /// @param name The new name for the production company.
    /// @return The updated production company.
    public ProductionCompany updateProductionCompany(UUID companyId, String name) {
        log.info("updating production company: {}", companyId);

        ProductionCompany company = getProductionCompany(companyId);
        company.setName(name);

        return companyRepository.save(company);
    }

}
