package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Genre;
import com.moviehub.entity.ProductionCompany;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing movie metadata, including countries, genres,
/// and production companies. This class provides methods for retrieving
/// saved entities and their details based on their identifiers.
@Service
@Transactional
@RequiredArgsConstructor
public class MovieMetadataService {

    /// Service for managing production company-related operations.
    private final ProductionCompanyService productionService;
    /// Service for managing country-related operations.
    private final CountryService countryService;
    /// Service for managing genre-related operations.
    private final GenreService genreService;

    /// Retrieves a list of saved countries based on the provided list.
    ///
    /// @param countries A list of Country entities to be retrieved.
    /// @return A list of saved Country entities.
    public List<Country> getSavedCountries(List<Country> countries) {
        return countryService.getSavedCountries(countries);
    }

    /// Retrieves a list of saved genres based on the provided list.
    ///
    /// @param genres A list of Genre entities to be retrieved.
    /// @return A list of saved Genre entities.
    public List<Genre> getSavedGenres(List<Genre> genres) {
        return genreService.getSavedGenres(genres);
    }

    /// Retrieves a list of saved production companies based on the provided list.
    ///
    /// @param production A list of ProductionCompany entities to be retrieved.
    /// @return A list of saved ProductionCompany entities.
    public List<ProductionCompany> getSavedProductions(List<ProductionCompany> production) {
        return productionService.getSavedProductions(production);
    }

    /// Retrieves a genre by its unique identifier.
    ///
    /// @param genreId The UUID of the Genre to be retrieved.
    /// @return The Genre entity associated with the specified identifier.
    public Genre getGenre(UUID genreId) {
        return genreService.getGenre(genreId);
    }

    /// Retrieves a country by its unique identifier.
    ///
    /// @param countryId The UUID of the Country to be retrieved.
    /// @return The Country entity associated with the specified identifier.
    public Country getCountry(UUID countryId) {
        return countryService.getCountry(countryId);
    }

    /// Retrieves a production company by its unique identifier.
    ///
    /// @param companyId The UUID of the ProductionCompany to be retrieved.
    /// @return The ProductionCompany entity associated with the specified identifier.
    public ProductionCompany getProductionCompany(UUID companyId) {
        return productionService.getProductionCompany(companyId);
    }

}
