package com.moviehub.service;

import com.moviehub.entity.Country;
import com.moviehub.entity.Genre;
import com.moviehub.entity.ProductionCompany;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieMetadataServiceTest {

    @Mock
    private ProductionCompanyService productionService;

    @Mock
    private CountryService countryService;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private MovieMetadataService movieMetadataService;

    private static final UUID COUNTRY_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID GENRE_ID = UUID.fromString("456e7890-e89b-12d3-a456-426614174001");
    private static final UUID COMPANY_ID = UUID.fromString("789e0123-e89b-12d3-a456-426614174002");

    @Test
    void shouldGetSavedCountries() {
        Country country = createCountry();
        List<Country> countries = List.of(country);

        when(countryService.getSavedCountries(countries)).thenReturn(countries);

        List<Country> savedCountries = movieMetadataService.getSavedCountries(countries);

        assertThat(savedCountries).isNotNull()
                                  .containsExactly(country);
    }

    @Test
    void shouldGetSavedGenres() {
        Genre genre = createGenre();
        List<Genre> genres = List.of(genre);

        when(genreService.getSavedGenres(genres)).thenReturn(genres);

        List<Genre> savedGenres = movieMetadataService.getSavedGenres(genres);

        assertThat(savedGenres).isNotNull()
                               .containsExactly(genre);
    }

    @Test
    void shouldGetSavedProductions() {
        ProductionCompany productionCompany = createProductionCompany();
        List<ProductionCompany> productions = List.of(productionCompany);

        when(productionService.getSavedProductions(productions)).thenReturn(productions);

        List<ProductionCompany> savedProductions = movieMetadataService.getSavedProductions(productions);

        assertThat(savedProductions).isNotNull()
                                    .containsExactly(productionCompany);
    }

    @Test
    void shouldGetGenre() {
        Genre genre = createGenre();

        when(genreService.getGenre(GENRE_ID)).thenReturn(genre);

        Genre foundGenre = movieMetadataService.getGenre(GENRE_ID);

        assertThat(foundGenre).isNotNull();
        assertThat(foundGenre.getId()).isEqualTo(GENRE_ID);
    }

    @Test
    void shouldGetCountry() {
        Country country = createCountry();

        when(countryService.getCountry(COUNTRY_ID)).thenReturn(country);

        Country foundCountry = movieMetadataService.getCountry(COUNTRY_ID);

        assertThat(foundCountry).isNotNull();
        assertThat(foundCountry.getId()).isEqualTo(COUNTRY_ID);
    }

    @Test
    void shouldGetProductionCompany() {
        ProductionCompany productionCompany = createProductionCompany();

        when(productionService.getProductionCompany(COMPANY_ID)).thenReturn(productionCompany);

        ProductionCompany foundCompany = movieMetadataService.getProductionCompany(COMPANY_ID);

        assertThat(foundCompany).isNotNull();
        assertThat(foundCompany.getId()).isEqualTo(COMPANY_ID);
    }

    private static ProductionCompany createProductionCompany() {
        return ProductionCompany.builder()
                                .id(COMPANY_ID)
                                .build();
    }

    private static Country createCountry() {
        return Country.builder()
                      .id(COUNTRY_ID)
                      .build();
    }

    private static Genre createGenre() {
        return Genre.builder()
                    .id(GENRE_ID)
                    .build();
    }

}