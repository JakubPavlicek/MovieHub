package com.moviehub.controller;

import com.moviehub.CountriesApi;
import com.moviehub.dto.AddCountryRequest;
import com.moviehub.dto.CountryDetailsResponse;
import com.moviehub.dto.CountryListResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.entity.Country;
import com.moviehub.entity.Movie;
import com.moviehub.mapper.dto.CountryMapper;
import com.moviehub.mapper.dto.MovieMapper;
import com.moviehub.service.CountryService;
import com.moviehub.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController implements CountriesApi {

    private final CountryService countryService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<CountryDetailsResponse> addCountry(AddCountryRequest addCountryRequest) {
        Country country = countryService.addCountry(addCountryRequest.getName());
        CountryDetailsResponse countryResponse = CountryMapper.mapToCountryDetailsResponse(country);

        return ResponseEntity.status(HttpStatus.CREATED).body(countryResponse);
    }

    @Override
    public ResponseEntity<CountryListResponse> getCountries() {
        List<Country> countries = countryService.getCountries();
        CountryListResponse countryListResponse = CountryMapper.mapToCountryListResponse(countries);

        return ResponseEntity.ok(countryListResponse);
    }

    @Override
    public ResponseEntity<CountryDetailsResponse> getCountryById(String countryId) {
        Country country = countryService.getCountry(countryId);
        CountryDetailsResponse countryResponse = CountryMapper.mapToCountryDetailsResponse(country);

        return ResponseEntity.ok(countryResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithCountry(String countryId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesWithCountry(countryId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

    @Override
    public ResponseEntity<CountryDetailsResponse> updateCountry(String countryId, AddCountryRequest addCountryRequest) {
        Country country = countryService.updateCountry(countryId, addCountryRequest.getName());
        CountryDetailsResponse countryResponse = CountryMapper.mapToCountryDetailsResponse(country);

        return ResponseEntity.ok(countryResponse);
    }

}
