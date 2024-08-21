package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Country_;
import com.moviehub.exception.CountryAlreadyExistsException;
import com.moviehub.exception.CountryNotFoundException;
import com.moviehub.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    @Transactional
    public List<Country> getSavedCountries(List<Country> countries) {
        return countries.stream()
                        .map(this::getSavedCountry)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    private Country getSavedCountry(Country country) {
        return countryRepository.findByName(country.getName())
                                .orElseThrow(() -> new CountryNotFoundException("Country with name: " + country.getName() + " not found"));
    }

    @Transactional
    public Country addCountry(String name) {
        if (countryRepository.existsByName(name)) {
            throw new CountryAlreadyExistsException("Country with name: " + name + " already exists");
        }

        Country country = new Country();
        country.setName(name);

        return countryRepository.save(country);
    }

    @Transactional
    public Page<Country> getCountries(Integer page, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, Country_.NAME);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return countryRepository.findAll(pageable);
    }

    @Transactional
    public Country getCountry(String countryId) {
        return countryRepository.findById(countryId)
                                .orElseThrow(() -> new CountryNotFoundException("Country with ID: " + countryId + " not found"));
    }

    @Transactional
    public Country updateCountry(String countryId, String name) {
        Country country = getCountry(countryId);
        country.setName(name);

        return countryRepository.save(country);
    }

}
