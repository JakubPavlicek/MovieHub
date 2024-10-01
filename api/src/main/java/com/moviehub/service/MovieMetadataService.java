package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Genre;
import com.moviehub.entity.ProductionCompany;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public Genre getGenre(UUID genreId) {
        return genreService.getGenre(genreId);
    }

    public Country getCountry(UUID countryId) {
        return countryService.getCountry(countryId);
    }

    public ProductionCompany getProductionCompany(UUID companyId) {
        return productionService.getProductionCompany(companyId);
    }

}
