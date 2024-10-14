package com.moviehub.mapper.dto;

import com.moviehub.dto.CountryDetailsResponse;
import com.moviehub.dto.CountryListResponse;
import com.moviehub.entity.Country;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CountryMapperTest {

    private static final String USA = "USA";
    private static final String CANADA = "Canada";
    private static final String FRANCE = "France";

    private static final UUID FIRST_COUNTRY_ID = UUID.fromString("bbefebac-b1d9-4a59-8a61-c04f2aeef660");
    private static final UUID SECOND_COUNTRY_ID = UUID.fromString("d519841e-5826-4de2-8aca-3510f9e54cfc");

    @Test
    void shouldMapToCountries() {
        List<String> countryNames = createCountryNamesList();

        List<Country> countries = CountryMapper.mapToCountries(countryNames);

        assertThat(countries).hasSize(3);
        assertThat(countries.get(0).getName()).isEqualTo(USA);
        assertThat(countries.get(1).getName()).isEqualTo(CANADA);
        assertThat(countries.get(2).getName()).isEqualTo(FRANCE);
    }

    @Test
    void shouldMapToCountryDetailsResponseList() {
        List<Country> countries = List.of(
            createCountry(FIRST_COUNTRY_ID, USA),
            createCountry(SECOND_COUNTRY_ID, CANADA)
        );

        List<CountryDetailsResponse> responseList = CountryMapper.mapToCountryDetailsResponseList(countries);

        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getId()).isEqualTo(FIRST_COUNTRY_ID);
        assertThat(responseList.get(0).getName()).isEqualTo(USA);
        assertThat(responseList.get(1).getId()).isEqualTo(SECOND_COUNTRY_ID);
        assertThat(responseList.get(1).getName()).isEqualTo(CANADA);
    }

    @Test
    void shouldMapToCountryNames() {
        List<Country> countries = List.of(
            createCountry(FIRST_COUNTRY_ID, USA),
            createCountry(SECOND_COUNTRY_ID, FRANCE)
        );

        List<String> countryNames = CountryMapper.mapToCountryNames(countries);

        assertThat(countryNames).containsExactly(USA, FRANCE);
    }

    @Test
    void shouldMapToCountryDetailsResponse() {
        Country country = createCountry(FIRST_COUNTRY_ID, USA);

        CountryDetailsResponse response = CountryMapper.mapToCountryDetailsResponse(country);

        assertThat(response.getId()).isEqualTo(FIRST_COUNTRY_ID);
        assertThat(response.getName()).isEqualTo(USA);
    }

    @Test
    void shouldMapToCountryListResponse() {
        List<Country> countries = List.of(
            createCountry(FIRST_COUNTRY_ID, USA),
            createCountry(SECOND_COUNTRY_ID, CANADA)
        );

        CountryListResponse countryListResponse = CountryMapper.mapToCountryListResponse(countries);

        assertThat(countryListResponse.getCountries()).hasSize(2);
        assertThat(countryListResponse.getCountries().get(0).getId()).isEqualTo(FIRST_COUNTRY_ID);
        assertThat(countryListResponse.getCountries().get(0).getName()).isEqualTo(USA);
        assertThat(countryListResponse.getCountries().get(1).getId()).isEqualTo(SECOND_COUNTRY_ID);
        assertThat(countryListResponse.getCountries().get(1).getName()).isEqualTo(CANADA);
    }
    
    private static Country createCountry(UUID id, String name) {
        return Country.builder()
                      .id(id)
                      .name(name)
                      .build();
    }

    private static List<String> createCountryNamesList() {
        return List.of(USA, CANADA, FRANCE);
    }

}