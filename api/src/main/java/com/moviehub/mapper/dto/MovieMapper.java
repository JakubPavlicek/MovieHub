package com.moviehub.mapper.dto;

import com.moviehub.dto.AddMovieRequest;
import com.moviehub.dto.MovieDetailsResponse;
import com.moviehub.dto.MoviePage;
import com.moviehub.dto.MoviePreviewResponse;
import com.moviehub.dto.MovieUserRating;
import com.moviehub.dto.UpdateMovieRequest;
import com.moviehub.dto.YearListResponse;
import com.moviehub.entity.Movie;
import org.springframework.data.domain.Page;

import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between Movie entities and Data Transfer Objects (DTOs).
public class MovieMapper {

    // Private constructor to prevent instantiation.
    private MovieMapper() {
    }

    /// Maps an AddMovieRequest DTO to a Movie entity.
    ///
    /// @param addMovieRequest The AddMovieRequest DTO to map.
    /// @return A Movie entity created from the provided request.
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

    /// Maps an UpdateMovieRequest DTO to a Movie entity.
    ///
    /// @param updateMovieRequest The UpdateMovieRequest DTO to map.
    /// @return A Movie entity created from the provided request.
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

    /// Maps a Movie entity to a MoviePreviewResponse DTO.
    ///
    /// @param movie The Movie entity to map.
    /// @return A MoviePreviewResponse DTO containing the movie's preview information.
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

    /// Maps a Movie entity to a MovieDetailsResponse DTO.
    ///
    /// @param movie The Movie entity to map.
    /// @return A MovieDetailsResponse DTO containing the movie's detailed information.
    public static MovieDetailsResponse mapToMovieDetailsResponse(Movie movie) {
        return MovieDetailsResponse.builder()
                                   .id(movie.getId())
                                   .name(movie.getName())
                                   .filename(movie.getFilename())
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

    /// Maps a Page of Movie entities to a MoviePage DTO.
    ///
    /// @param movies A Page of Movie entities to map.
    /// @return A MoviePage DTO containing the paginated movie information.
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

    /// Maps a Page of Movie entities to a list of MoviePreviewResponse DTOs.
    ///
    /// @param movies A Page of Movie entities to map.
    /// @return A list of MoviePreviewResponse DTOs containing preview information for each movie.
    private static List<MoviePreviewResponse> mapToMoviePreviewResponseList(Page<Movie> movies) {
        return movies.stream()
                     .map(MovieMapper::mapToMoviePreviewResponse)
                     .toList();
    }

    /// Maps a list of years to a YearListResponse DTO.
    ///
    /// @param years A list of years to map.
    /// @return A YearListResponse DTO containing the list of years.
    public static YearListResponse mapToYearListResponse(List<Integer> years) {
        return YearListResponse.builder()
                               .years(years)
                               .build();
    }

    /// Maps a rating value to a MovieUserRating DTO.
    ///
    /// @param rating The rating value to map.
    /// @return A MovieUserRating DTO containing the rating information.
    public static MovieUserRating mapToMovieUserRating(Double rating) {
        return MovieUserRating.builder()
                              .rating(rating)
                              .build();
    }

}
