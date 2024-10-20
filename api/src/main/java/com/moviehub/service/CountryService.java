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

@Service
@Transactional
@Log4j2
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public List<Country> getSavedCountries(List<Country> countries) {
        log.info("retrieving saved countries");

        return countries.stream()
                        .map(this::getSavedCountry)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    private Country getSavedCountry(Country country) {
        return countryRepository.findByName(country.getName())
                                .orElseThrow(() -> new CountryNotFoundException("Country with name: " + country.getName() + " not found"));
    }

    public List<Country> getCountries() {
        log.info("retrieving all countries");

        Sort sort = Sort.by(Country_.NAME).ascending();

        return countryRepository.findAll(sort);
    }

    public Country getCountry(UUID countryId) {
        log.info("retrieving country: {}", countryId);

        return countryRepository.findById(countryId)
                                .orElseThrow(() -> new CountryNotFoundException("Country with ID: " + countryId + " not found"));
    }

    public Country addCountry(String name) {
        if (countryRepository.existsByName(name)) {
            throw new CountryAlreadyExistsException("Country with name: " + name + " already exists");
        }

        log.info("adding new country: {}", name);

        Country country = new Country();
        country.setName(name);

        return countryRepository.save(country);
    }

    public Country updateCountry(UUID countryId, String name) {
        log.info("updating country: {} to name: {}", countryId, name);

        Country country = getCountry(countryId);
        country.setName(name);

        return countryRepository.save(country);
    }

}
