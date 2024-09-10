package com.moviehub.mapper.dto;

import com.moviehub.dto.CountryDetailsResponse;
import com.moviehub.dto.CountryListResponse;
import com.moviehub.entity.Country;

import java.util.List;

public class CountryMapper {

    private CountryMapper() {
    }

    public static List<Country> mapToCountries(List<String> countries) {
        return countries.stream()
                        .map(countryName -> Country.builder()
                                                   .name(countryName)
                                                   .build())
                        .toList();
    }

    public static List<CountryDetailsResponse> mapToCountryDetailsResponseList(List<Country> countries) {
        return countries.stream()
                        .map(CountryMapper::mapToCountryDetailsResponse)
                        .toList();
    }

    public static List<String> mapToCountryNames(List<Country> countries) {
        return countries.stream()
                        .map(Country::getName)
                        .toList();
    }

    public static CountryDetailsResponse mapToCountryDetailsResponse(Country country) {
        return CountryDetailsResponse.builder()
                                     .id(country.getId())
                                     .name(country.getName())
                                     .build();
    }

    public static CountryListResponse mapToCountryListResponse(List<Country> countries) {
        return CountryListResponse.builder()
                                  .countries(mapToCountryDetailsResponseList(countries))
                                  .build();
    }

}
