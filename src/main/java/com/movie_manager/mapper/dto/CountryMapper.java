package com.movie_manager.mapper.dto;

import com.movie_manager.dto.CountryDTO;
import com.movie_manager.entity.Country;

import java.util.List;

public class CountryMapper {

    private CountryMapper() {
    }

    public static List<Country> mapToCountries(List<String> countries) {
        return countries.stream()
                        .map(countryName -> Country.builder().name(countryName).build())
                        .toList();
    }

    public static List<CountryDTO> mapToCountryDTOS(List<Country> countries) {
        return countries.stream()
                        .map(CountryMapper::mapToCountryDTO)
                        .toList();
    }

    public static List<String> mapToCountryNames(List<Country> countries) {
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
