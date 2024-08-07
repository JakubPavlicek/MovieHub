package com.movie_manager.controller;

import com.movie_manager.ProductionCompaniesApi;
import com.movie_manager.dto.AddProductionCompanyRequest;
import com.movie_manager.dto.MoviePage;
import com.movie_manager.dto.ProductionCompanyDTO;
import com.movie_manager.dto.ProductionCompanyResponse;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductionCompanyController implements ProductionCompaniesApi {

    private final ProductionCompanyService companyService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<ProductionCompanyDTO> addProductionCompany(AddProductionCompanyRequest addProductionCompanyRequest) {
        ProductionCompany company = companyService.addProductionCompany(addProductionCompanyRequest.getName());
        ProductionCompanyDTO productionCompanyDTO = ProductionCompanyMapper.mapToProductionCompanyDTO(company);

        return ResponseEntity.status(HttpStatus.CREATED).body(productionCompanyDTO);
    }

    @Override
    public ResponseEntity<ProductionCompanyResponse> getProductionCompanies() {
        List<ProductionCompany> companies = companyService.getProductionCompanies();
        ProductionCompanyResponse productionCompanyResponse = ProductionCompanyMapper.mapToProductionCompanyResponse(companies);

        return ResponseEntity.ok(productionCompanyResponse);
    }

    @Override
    public ResponseEntity<ProductionCompanyDTO> getProductionCompanyById(String companyId) {
        ProductionCompany company = companyService.getProductionCompany(companyId);
        ProductionCompanyDTO companyDTO = ProductionCompanyMapper.mapToProductionCompanyDTO(company);

        return ResponseEntity.ok(companyDTO);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithProductionCompany(String companyId, Integer page, Integer limit, String sort) {
        Page<Movie> movies = movieService.getMoviesWithProductionCompany(companyId, page, limit, sort);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
