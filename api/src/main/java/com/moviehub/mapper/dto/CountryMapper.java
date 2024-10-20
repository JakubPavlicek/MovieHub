package com.moviehub.mapper.dto;

import com.moviehub.dto.CountryDetailsResponse;
import com.moviehub.dto.CountryListResponse;
import com.moviehub.entity.Country;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between Country entities and Data Transfer Objects (DTOs).
public class CountryMapper {

    // Private constructor to prevent instantiation.
    private CountryMapper() {
    }

    /// Maps a list of country names to a list of Country entities.
    ///
    /// @param countries The list of country names to map.
    /// @return A list of Country entities.
    public static List<Country> mapToCountries(List<String> countries) {
        return countries.stream()
                        .map(countryName -> Country.builder()
                                                   .name(countryName)
                                                   .build())
                        .toList();
    }

    /// Maps a list of Country entities to a list of CountryDetailsResponse DTOs.
    ///
    /// @param countries The list of Country entities to map.
    /// @return A list of CountryDetailsResponse DTOs.
    public static List<CountryDetailsResponse> mapToCountryDetailsResponseList(List<Country> countries) {
        return countries.stream()
                        .map(CountryMapper::mapToCountryDetailsResponse)
                        .toList();
    }

    /// Maps a list of Country entities to a list of country names.
    ///
    /// @param countries The list of Country entities to map.
    /// @return A list of country names.
    public static List<String> mapToCountryNames(List<Country> countries) {
        return countries.stream()
                        .map(Country::getName)
                        .toList();
    }

    /// Maps a Country entity to a CountryDetailsResponse DTO.
    ///
    /// @param country The Country entity to map.
    /// @return A CountryDetailsResponse DTO containing the country information.
    public static CountryDetailsResponse mapToCountryDetailsResponse(Country country) {
        return CountryDetailsResponse.builder()
                                     .id(country.getId())
                                     .name(country.getName())
                                     .build();
    }

    /// Maps a list of Country entities to a CountryListResponse DTO.
    ///
    /// @param countries The list of Country entities to map.
    /// @return A CountryListResponse DTO containing the list of countries.
    public static CountryListResponse mapToCountryListResponse(List<Country> countries) {
        return CountryListResponse.builder()
                                  .countries(mapToCountryDetailsResponseList(countries))
                                  .build();
    }

}
