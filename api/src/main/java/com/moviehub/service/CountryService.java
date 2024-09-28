package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Country_;
import com.moviehub.exception.CountryAlreadyExistsException;
import com.moviehub.exception.CountryNotFoundException;
import com.moviehub.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public List<Country> getSavedCountries(List<Country> countries) {
        return countries.stream()
                        .map(this::getSavedCountry)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    private Country getSavedCountry(Country country) {
        return countryRepository.findByName(country.getName())
                                .orElseThrow(() -> new CountryNotFoundException("Country with name: " + country.getName() + " not found"));
    }

    public Country addCountry(String name) {
        if (countryRepository.existsByName(name)) {
            throw new CountryAlreadyExistsException("Country with name: " + name + " already exists");
        }

        Country country = new Country();
        country.setName(name);

        return countryRepository.save(country);
    }

    public List<Country> getCountries() {
        Sort sort = Sort.by(Sort.Direction.ASC, Country_.NAME);

        return countryRepository.findAll(sort);
    }

    public Country getCountry(String countryId) {
        return countryRepository.findById(countryId)
                                .orElseThrow(() -> new CountryNotFoundException("Country with ID: " + countryId + " not found"));
    }

    public Country updateCountry(String countryId, String name) {
        Country country = getCountry(countryId);
        country.setName(name);

        return countryRepository.save(country);
    }

}
