package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Genre;
import com.moviehub.entity.ProductionCompany;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieMetadataService {

    private final ProductionCompanyService productionService;
    private final CountryService countryService;
    private final GenreService genreService;

    public List<Country> getSavedCountries(List<Country> countries) {
        return countryService.getSavedCountries(countries);
    }

    public List<Genre> getSavedGenres(List<Genre> genres) {
        return genreService.getSavedGenres(genres);
    }

    public List<ProductionCompany> getSavedProductions(List<ProductionCompany> production) {
        return productionService.getSavedProductions(production);
    }

    public Genre getGenre(String genreId) {
        return genreService.getGenre(genreId);
    }

    public Country getCountry(String countryId) {
        return countryService.getCountry(countryId);
    }

    public ProductionCompany getProductionCompany(String companyId) {
        return productionService.getProductionCompany(companyId);
    }

}
