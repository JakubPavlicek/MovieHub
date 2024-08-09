package com.movie_manager.controller;

import com.movie_manager.ProductionCompaniesApi;
import com.movie_manager.dto.AddProductionCompanyRequest;
import com.movie_manager.dto.MoviePage;
import com.movie_manager.dto.ProductionCompanyDetailsResponse;
import com.movie_manager.dto.ProductionCompanyPage;
import com.movie_manager.entity.Movie;
import com.movie_manager.entity.ProductionCompany;
import com.movie_manager.mapper.dto.MovieMapper;
import com.movie_manager.mapper.dto.ProductionCompanyMapper;
import com.movie_manager.service.MovieService;
import com.movie_manager.service.ProductionCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductionCompanyController implements ProductionCompaniesApi {

    private final ProductionCompanyService companyService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<ProductionCompanyDetailsResponse> addProductionCompany(AddProductionCompanyRequest addProductionCompanyRequest) {
        ProductionCompany company = companyService.addProductionCompany(addProductionCompanyRequest.getName());
        ProductionCompanyDetailsResponse companyDetailsResponse = ProductionCompanyMapper.mapToProductionCompanyDetailsResponse(company);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyDetailsResponse);
    }

    @Override
    public ResponseEntity<ProductionCompanyPage> getProductionCompanies(Integer page, Integer limit) {
        Page<ProductionCompany> companies = companyService.getProductionCompanies(page, limit);
        ProductionCompanyPage companyPage = ProductionCompanyMapper.mapToProductionCompanyPage(companies);

        return ResponseEntity.ok(companyPage);
    }

    @Override
    public ResponseEntity<ProductionCompanyDetailsResponse> getProductionCompanyById(String companyId) {
        ProductionCompany company = companyService.getProductionCompany(companyId);
        ProductionCompanyDetailsResponse companyDetailsResponse = ProductionCompanyMapper.mapToProductionCompanyDetailsResponse(company);

        return ResponseEntity.ok(companyDetailsResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithProductionCompany(String companyId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithProductionCompany(companyId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
