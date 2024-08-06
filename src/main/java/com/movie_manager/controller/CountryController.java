package com.movie_manager.controller;

import com.movie_manager.CountriesApi;
import com.movie_manager.dto.AddCountryRequest;
import com.movie_manager.dto.CountryDTO;
import com.movie_manager.dto.CountryResponse;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController implements CountriesApi {

    private final CountryService countryService;
    private final MovieService movieService;

    @Override
    public ResponseEntity<CountryDTO> addCountry(AddCountryRequest addCountryRequest) {
        Country country = countryService.addCountry(addCountryRequest.getName());
        CountryDTO countryDTO = CountryMapper.mapToCountryDTO(country);

        return ResponseEntity.status(HttpStatus.CREATED).body(countryDTO);
    }

    @Override
    public ResponseEntity<CountryResponse> getCountries() {
        List<Country> countries = countryService.getCountries();
        CountryResponse countryResponse = CountryMapper.mapToCountryResponse(countries);

        return ResponseEntity.ok(countryResponse);
    }

    @Override
    public ResponseEntity<CountryDTO> getCountryById(String countryId) {
        Country country = countryService.getCountry(countryId);
        CountryDTO countryDTO = CountryMapper.mapToCountryDTO(country);

        return ResponseEntity.ok(countryDTO);
    }

    @Override
    public ResponseEntity<MoviePage> getMoviesWithCountry(String countryId, Integer page, Integer limit, String sort) {
        Page<Movie> movies = movieService.getMoviesByCountry(countryId, page, limit, sort);
        MoviePage moviePage = MovieMapper.mapToMoviePage(movies);

        return ResponseEntity.ok(moviePage);
    }

}
