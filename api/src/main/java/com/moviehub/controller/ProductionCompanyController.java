package com.moviehub.controller;

import com.moviehub.ProductionCompaniesApi;
import com.moviehub.dto.AddProductionCompanyRequest;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.ProductionCompanyDetailsResponse;
import com.moviehub.dto.ProductionCompanyPage;
import com.moviehub.entity.Movie;
import com.moviehub.entity.ProductionCompany;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.mapper.dto.ProductionCompanyMapper;
import com.moviehub.service.MovieService;
import com.moviehub.service.ProductionCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Controller class for managing production companies.
/// It provides endpoints for performing operations related to production companies and their associated movies.
@RestController
@RequiredArgsConstructor
public class ProductionCompanyController implements ProductionCompaniesApi {

    /// Service for handling operations related to production companies.
    private final ProductionCompanyService companyService;
    /// Service for handling operations related to movies.
    private final MovieService movieService;

    @Override
    public ResponseEntity<ProductionCompanyDetailsResponse> addProductionCompany(AddProductionCompanyRequest addProductionCompanyRequest) {
        ProductionCompany company = companyService.addProductionCompany(addProductionCompanyRequest.getName());
        ProductionCompanyDetailsResponse companyDetailsResponse = ProductionCompanyMapper.mapToProductionCompanyDetailsResponse(company);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyDetailsResponse);
    }

    @Override
    public ResponseEntity<ProductionCompanyPage> getProductionCompanies(Integer page, Integer limit, String name) {
        Page<ProductionCompany> companies = companyService.getProductionCompanies(page, limit, name);
        ProductionCompanyPage companyPage = ProductionCompanyMapper.mapToProductionCompanyPage(companies);

        return ResponseEntity.ok(companyPage);
    }

    @Override
    public ResponseEntity<ProductionCompanyDetailsResponse> getProductionCompany(UUID companyId) {
        ProductionCompany company = companyService.getProductionCompany(companyId);
        ProductionCompanyDetailsResponse companyDetailsResponse = ProductionCompanyMapper.mapToProductionCompanyDetailsResponse(company);

        return ResponseEntity.ok(companyDetailsResponse);
    }

    @Override
    public ResponseEntity<ProductionCompanyDetailsResponse> updateProductionCompany(UUID companyId, AddProductionCompanyRequest addProductionCompanyRequest) {
        ProductionCompany company = companyService.updateProductionCompany(companyId, addProductionCompanyRequest.getName());
        ProductionCompanyDetailsResponse companyResponse = ProductionCompanyMapper.mapToProductionCompanyDetailsResponse(company);

        return ResponseEntity.ok(companyResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithProductionCompany(UUID companyId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithProductionCompany(companyId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
