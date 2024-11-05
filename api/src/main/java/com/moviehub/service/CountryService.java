package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Country_;
import com.moviehub.exception.CountryAlreadyExistsException;
import com.moviehub.exception.CountryNotFoundException;
import com.moviehub.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Service class for managing countries.
/// This class provides methods for adding, retrieving, and updating country entities.
@Service
@Transactional
@Log4j2
@AllArgsConstructor
public class CountryService {

    /// Repository for managing Country entities.
    private final CountryRepository countryRepository;

    /// Retrieves saved countries by checking their existence in the database.
    ///
    /// @param countries The list of countries to check for existence.
    /// @return A list of saved Country entities.
    public List<Country> getSavedCountries(List<Country> countries) {
        log.info("retrieving saved countries");

        return countries.stream()
                        .map(this::getSavedCountry)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    /// Retrieves a saved country by its name.
    ///
    /// @param country The country to check.
    /// @return The Country entity associated with the specified name.
    /// @throws CountryNotFoundException if the country does not exist.
    private Country getSavedCountry(Country country) {
        return countryRepository.findByName(country.getName())
                                .orElseThrow(() -> new CountryNotFoundException("Country with name: " + country.getName() + " not found"));
    }

    /// Retrieves all countries from the database, sorted by name in ascending order.
    ///
    /// @return A list of all Country entities.
    public List<Country> getCountries() {
        log.info("retrieving all countries");

        Sort sort = Sort.by(Country_.NAME).ascending();

        return countryRepository.findAll(sort);
    }

    /// Retrieves a country by its ID.
    ///
    /// @param countryId The ID of the country to retrieve.
    /// @return The Country entity associated with the specified ID.
    /// @throws CountryNotFoundException if the country does not exist.
    public Country getCountry(UUID countryId) {
        log.info("retrieving country: {}", countryId);

        return countryRepository.findById(countryId)
                                .orElseThrow(() -> new CountryNotFoundException("Country with ID: " + countryId + " not found"));
    }

    /// Adds a new country to the database.
    ///
    /// @param name The name of the country to add.
    /// @return The newly created Country entity.
    /// @throws CountryAlreadyExistsException if a country with the same name already exists.
    public Country addCountry(String name) {
        if (countryRepository.existsByName(name)) {
            throw new CountryAlreadyExistsException("Country with name: " + name + " already exists");
        }

        log.info("adding new country: {}", name);

        Country country = new Country();
        country.setName(name);

        return countryRepository.save(country);
    }

    /// Updates the name of an existing country.
    ///
    /// @param countryId The ID of the country to update.
    /// @param name The new name for the country.
    /// @return The updated Country entity.
    /// @throws CountryNotFoundException if the country does not exist.
    public Country updateCountry(UUID countryId, String name) {
        log.info("updating country: {} to name: {}", countryId, name);

        Country country = getCountry(countryId);
        country.setName(name);

        return countryRepository.save(country);
    }

}
