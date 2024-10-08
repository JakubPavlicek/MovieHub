package com.moviehub.mapper.dto;

import com.moviehub.dto.AddMovieRequest;
import com.moviehub.dto.MovieDetailsResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.MoviePreviewResponse;
import com.moviehub.dto.UpdateMovieRequest;
import com.moviehub.dto.YearListResponse;
import com.moviehub.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {

    private MovieMapper() {
    }

    public static Movie mapToMovie(AddMovieRequest addMovieRequest) {
        return Movie.builder()
                    .name(addMovieRequest.getName())
                    .filename(addMovieRequest.getFilename())
                    .releaseDate(addMovieRequest.getReleaseDate())
                    .duration(addMovieRequest.getDuration())
                    .description(addMovieRequest.getDescription())
                    .posterUrl(addMovieRequest.getPosterUrl())
                    .trailerUrl(addMovieRequest.getTrailerUrl())
                    .director(DirectorMapper.mapToDirector(addMovieRequest.getDirector()))
                    .cast(MovieCastMapper.mapToMovieCasts(addMovieRequest.getCast()))
                    .production(ProductionCompanyMapper.mapToProductionCompanies(addMovieRequest.getProductionCompanies()))
                    .genres(GenreMapper.mapToGenres(addMovieRequest.getGenres()))
                    .countries(CountryMapper.mapToCountries(addMovieRequest.getCountries()))
                    .build();
    }

    public static Movie mapToMovie(UpdateMovieRequest updateMovieRequest) {
        return Movie.builder()
                    .name(updateMovieRequest.getName())
                    .filename(updateMovieRequest.getFilename())
                    .releaseDate(updateMovieRequest.getReleaseDate())
                    .duration(updateMovieRequest.getDuration())
                    .description(updateMovieRequest.getDescription())
                    .posterUrl(updateMovieRequest.getPosterUrl())
                    .trailerUrl(updateMovieRequest.getTrailerUrl())
                    .director(DirectorMapper.mapToDirector(updateMovieRequest.getDirector()))
                    .cast(MovieCastMapper.mapToMovieCasts(updateMovieRequest.getCast()))
                    .production(ProductionCompanyMapper.mapToProductionCompanies(updateMovieRequest.getProductionCompanies()))
                    .genres(GenreMapper.mapToGenres(updateMovieRequest.getGenres()))
                    .countries(CountryMapper.mapToCountries(updateMovieRequest.getCountries()))
                    .build();
    }

    public static MoviePreviewResponse mapToMoviePreviewResponse(Movie movie) {
        return MoviePreviewResponse.builder()
                                   .id(movie.getId())
                                   .name(movie.getName())
                                   .releaseYear(movie.getReleaseDate().getYear())
                                   .duration(movie.getDuration())
                                   .posterUrl(movie.getPosterUrl())
                                   .genres(GenreMapper.mapToGenreNames(movie.getGenres()))
                                   .build();
    }

    public static MovieDetailsResponse mapToMovieDetailsResponse(Movie movie) {
        return MovieDetailsResponse.builder()
                                   .id(movie.getId())
                                   .name(movie.getName())
                                   .releaseDate(movie.getReleaseDate())
                                   .duration(movie.getDuration())
                                   .description(movie.getDescription())
                                   .rating(movie.getRating())
                                   .reviewCount(movie.getReviewCount())
                                   .posterUrl(movie.getPosterUrl())
                                   .trailerUrl(movie.getTrailerUrl())
                                   .director(DirectorMapper.mapToDirectorResponse(movie.getDirector()))
                                   .cast(MovieCastMapper.mapToMovieCastDetailsResponse(movie.getCast()))
                                   .productionCompanies(ProductionCompanyMapper.mapToProductionCompanyDetailsResponseList(movie.getProduction()))
                                   .genres(GenreMapper.mapToGenreDetailsResponseList(movie.getGenres()))
                                   .countries(CountryMapper.mapToCountryDetailsResponseList(movie.getCountries()))
                                   .build();
    }

    public static MovieDetailsResponse mapToMovieDetailsResponse(Movie movie, Double rating) {
        return MovieDetailsResponse.builder()
                                   .id(movie.getId())
                                   .name(movie.getName())
                                   .releaseDate(movie.getReleaseDate())
                                   .duration(movie.getDuration())
                                   .description(movie.getDescription())
                                   .rating(movie.getRating())
                                   .reviewCount(movie.getReviewCount())
                                   .userRating(rating)
                                   .posterUrl(movie.getPosterUrl())
                                   .trailerUrl(movie.getTrailerUrl())
                                   .director(DirectorMapper.mapToDirectorResponse(movie.getDirector()))
                                   .cast(MovieCastMapper.mapToMovieCastDetailsResponse(movie.getCast()))
                                   .productionCompanies(ProductionCompanyMapper.mapToProductionCompanyDetailsResponseList(movie.getProduction()))
                                   .genres(GenreMapper.mapToGenreDetailsResponseList(movie.getGenres()))
                                   .countries(CountryMapper.mapToCountryDetailsResponseList(movie.getCountries()))
                                   .build();
    }

    public static MoviePage mapToMoviePage(Page<Movie> movies) {
        return MoviePage.builder()
                        .content(mapToMoviePreviewResponseList(movies))
                        .pageable(PageableMapper.mapToPageableDTO(movies.getPageable()))
                        .last(movies.isLast())
                        .totalElements(movies.getTotalElements())
                        .totalPages(movies.getTotalPages())
                        .first(movies.isFirst())
                        .size(movies.getSize())
                        .number(movies.getNumber())
                        .sort(SortMapper.mapToSortDTO(movies.getSort()))
                        .numberOfElements(movies.getNumberOfElements())
                        .empty(movies.isEmpty())
                        .build();
    }

    private static List<MoviePreviewResponse> mapToMoviePreviewResponseList(Page<Movie> movies) {
        return movies.stream()
                     .map(MovieMapper::mapToMoviePreviewResponse)
                     .toList();
    }

    public static YearListResponse mapToYearListResponse(List<Integer> years) {
        return YearListResponse.builder()
                               .years(years)
                               .build();
    }

}
