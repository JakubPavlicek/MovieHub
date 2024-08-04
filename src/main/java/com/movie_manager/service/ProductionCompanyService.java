package com.movie_manager.service;

import com.movie_manager.entity.ProductionCompany;
import com.movie_manager.repository.ProductionCompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionCompanyService {

    private final ProductionCompanyRepository companyRepository;

    @Transactional
    public List<ProductionCompany> getSavedProduction(List<ProductionCompany> companies) {
        List<ProductionCompany> savedProduction = new ArrayList<>();

        for (ProductionCompany company : companies) {
            ProductionCompany savedCompany = companyRepository.findByName(company.getName())
                                                              .orElseGet(() -> companyRepository.save(company));
            savedProduction.add(savedCompany);
        }

        return savedProduction;
    }

}
