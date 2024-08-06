package com.movie_manager.service;

import com.movie_manager.entity.Country;
import com.movie_manager.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
                                .orElseGet(() -> countryRepository.save(country));
    }


}
