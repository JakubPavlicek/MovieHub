package com.movie_manager.service;

import com.movie_manager.entity.Country;
import com.movie_manager.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    @Transactional
    public List<Country> getSavedCountries(List<Country> countries) {
        List<Country> savedCountries = new ArrayList<>();

        for (Country country : countries) {
            Country existingCountry = countryRepository.findByName(country.getName())
                                                       .orElseGet(() -> countryRepository.save(country));
            savedCountries.add(existingCountry);
        }

        return savedCountries;
    }

}
