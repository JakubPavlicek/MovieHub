package com.moviehub.mapper.dto;

import com.moviehub.dto.CountryDetailsResponse;
import com.moviehub.dto.CountryPage;
import com.moviehub.entity.Country;
import org.springframework.data.domain.Page;

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
                                     .countryId(country.getCountryId())
                                     .name(country.getName())
                                     .build();
    }

    public static CountryPage mapToCountryPage(Page<Country> countries) {
        return CountryPage.builder()
                          .content(mapToCountryDetailsResponseList(countries))
                          .pageable(PageableMapper.mapToPageableDTO(countries.getPageable()))
                          .last(countries.isLast())
                          .totalElements(countries.getTotalElements())
                          .totalPages(countries.getTotalPages())
                          .first(countries.isFirst())
                          .size(countries.getSize())
                          .number(countries.getNumber())
                          .sort(SortMapper.mapToSortDTO(countries.getSort()))
                          .numberOfElements(countries.getNumberOfElements())
                          .empty(countries.isEmpty())
                          .build();
    }

    private static List<CountryDetailsResponse> mapToCountryDetailsResponseList(Page<Country> countries) {
        return countries.stream()
                        .map(CountryMapper::mapToCountryDetailsResponse)
                        .toList();
    }

}
