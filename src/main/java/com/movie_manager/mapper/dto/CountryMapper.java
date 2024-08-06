package com.movie_manager.mapper.dto;

import com.movie_manager.dto.CountryDTO;
import com.movie_manager.entity.Country;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CountryMapper {

    private CountryMapper() {
    }

    public static Set<Country> mapToCountries(List<String> countries) {
        return countries.stream()
                        .map(countryName -> Country.builder().name(countryName).build())
                        .collect(Collectors.toSet());
    }

    public static List<CountryDTO> mapToCountryDTOS(Set<Country> countries) {
        return countries.stream()
                        .map(CountryMapper::mapToCountryDTO)
                        .toList();
    }

    public static List<String> mapToCountryNames(Set<Country> countries) {
        return countries.stream()
                        .map(Country::getName)
                        .toList();
    }

    private static CountryDTO mapToCountryDTO(Country country) {
        return CountryDTO.builder()
                         .countryId(country.getCountryId())
                         .name(country.getName())
                         .build();
    }

}
