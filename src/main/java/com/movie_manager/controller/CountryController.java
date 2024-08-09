package com.movie_manager.controller;

import com.movie_manager.CountriesApi;
import com.movie_manager.dto.AddCountryRequest;
import com.movie_manager.dto.CountryDetailsResponse;
import com.movie_manager.dto.CountryPage;
import com.movie_manager.dto.MoviePage;
import com.movie_manager.entity.Country;
import com.movie_manager.entity.Movie;
import com.movie_manager.mapper.dto.CountryMapper;
import com.movie_manager.mapper.dto.MovieMapper;
import com.movie_manager.service.CountryService;
import com.movie_manager.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<CountryPage> getCountries(Integer page, Integer limit) {
        Page<Country> countries = countryService.getCountries(page, limit);
        CountryPage countryPage = CountryMapper.mapToCountryPage(countries);

        return ResponseEntity.ok(countryPage);
    }

    @Override
    public ResponseEntity<CountryDetailsResponse> getCountryById(String countryId) {
        Country country = countryService.getCountry(countryId);
        CountryDetailsResponse countryResponse = CountryMapper.mapToCountryDetailsResponse(country);

        return ResponseEntity.ok(countryResponse);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithCountry(String countryId, Integer page, Integer limit) {
        Page<Movie> movies = movieService.getMoviesByCountry(countryId, page, limit);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
