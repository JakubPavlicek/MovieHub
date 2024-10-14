package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Country_;
import com.moviehub.exception.CountryAlreadyExistsException;
import com.moviehub.exception.CountryNotFoundException;
import com.moviehub.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private static final UUID COUNTRY_ID = UUID.fromString("a45fbb39-d4ae-4559-ab3e-a41d5c5f3b05");
    private static final String FIRST_COUNTRY_NAME = "Country 1";
    private static final String SECOND_COUNTRY_NAME = "Country 2";

    @Test
    void shouldGetSavedCountries() {
        Country country = createCountry(FIRST_COUNTRY_NAME);
        List<Country> countries = List.of(country);

        when(countryRepository.findByName(FIRST_COUNTRY_NAME)).thenReturn(Optional.of(country));

        List<Country> savedCountries = countryService.getSavedCountries(countries);

        assertThat(savedCountries).containsExactly(country);
    }

    @Test
    void shouldThrowCountryNotFoundExceptionWhenCountryNotFound() {
        List<Country> countries = List.of(createCountry(FIRST_COUNTRY_NAME));

        when(countryRepository.findByName(FIRST_COUNTRY_NAME)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CountryNotFoundException.class)
            .isThrownBy(() -> countryService.getSavedCountries(countries));
    }

    @Test
    void shouldAddCountry() {
        when(countryRepository.existsByName(FIRST_COUNTRY_NAME)).thenReturn(false);
        when(countryRepository.save(any(Country.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Country addedCountry = countryService.addCountry(FIRST_COUNTRY_NAME);

        assertThat(addedCountry.getName()).isEqualTo(FIRST_COUNTRY_NAME);
    }

    @Test
    void shouldThrowCountryAlreadyExistsExceptionWhenCountryExists() {
        when(countryRepository.existsByName(FIRST_COUNTRY_NAME)).thenReturn(true);

        assertThatExceptionOfType(CountryAlreadyExistsException.class)
            .isThrownBy(() -> countryService.addCountry(FIRST_COUNTRY_NAME));
    }

    @Test
    void shouldGetCountries() {
        Country country1 = createCountry(FIRST_COUNTRY_NAME);
        Country country2 = createCountry(SECOND_COUNTRY_NAME);
        Sort sort = Sort.by(Country_.NAME)
                        .ascending();

        when(countryRepository.findAll(sort)).thenReturn(List.of(country1, country2));

        List<Country> countries = countryService.getCountries();

        assertThat(countries).containsExactlyInAnyOrder(country1, country2);
    }

    @Test
    void shouldGetCountry() {
        Country country = createCountry();

        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(country));

        Country foundCountry = countryService.getCountry(COUNTRY_ID);

        assertThat(foundCountry).isEqualTo(country);
    }

    @Test
    void shouldThrowCountryNotFoundExceptionWhenGettingNonExistentCountry() {
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CountryNotFoundException.class)
            .isThrownBy(() -> countryService.getCountry(COUNTRY_ID));
    }

    @Test
    void shouldUpdateCountry() {
        Country existingCountry = createCountry();

        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(existingCountry));
        when(countryRepository.save(any(Country.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Country updatedCountry = countryService.updateCountry(COUNTRY_ID, SECOND_COUNTRY_NAME);

        assertThat(updatedCountry.getName()).isEqualTo(SECOND_COUNTRY_NAME);
    }

    @Test
    void shouldThrowCountryNotFoundExceptionWhenUpdatingNonExistentCountry() {
        when(countryRepository.findById(COUNTRY_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(CountryNotFoundException.class)
            .isThrownBy(() -> countryService.updateCountry(COUNTRY_ID, FIRST_COUNTRY_NAME));
    }

    private static Country createCountry() {
        return Country.builder()
                      .id(COUNTRY_ID)
                      .name(FIRST_COUNTRY_NAME)
                      .build();
    }

    private static Country createCountry(String name) {
        return Country.builder()
                      .name(name)
                      .build();
    }

}