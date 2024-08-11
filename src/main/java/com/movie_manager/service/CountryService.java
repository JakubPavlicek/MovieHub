package com.movie_manager.service;

import com.movie_manager.entity.Country;
import com.movie_manager.entity.Country_;
import com.movie_manager.exception.CountryAlreadyExistsException;
import com.movie_manager.exception.CountryNotFoundException;
import com.movie_manager.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    @Transactional
    public Set<Country> getSavedCountries(Set<Country> countries) {
        return countries.stream()
                        .map(this::getSavedCountry)
                        .collect(Collectors.toCollection(HashSet::new));
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
