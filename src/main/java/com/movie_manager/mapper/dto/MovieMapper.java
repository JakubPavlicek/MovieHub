package com.movie_manager.mapper.dto;

import com.movie_manager.dto.AddMovieRequest;
import com.movie_manager.dto.MovieDetailsResponse;
import com.movie_manager.dto.MoviePage;
import com.movie_manager.dto.MovieResponse;
import com.movie_manager.dto.UpdateMovieRequest;
import com.movie_manager.entity.Movie;
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

    public static MovieResponse mapToMovieResponse(Movie movie) {
        return MovieResponse.builder()
                            .movieId(movie.getMovieId())
                            .name(movie.getName())
                            .releaseDate(movie.getReleaseDate())
                            .duration(movie.getDuration())
                            .description(movie.getDescription())
                            .rating(movie.getRating())
                            .reviewCount(movie.getReviewCount())
                            .posterUrl(movie.getPosterUrl())
                            .trailerUrl(movie.getTrailerUrl())
                            .director(movie.getDirector().getName())
                            .cast(MovieCastMapper.mapToMovieCastResponse(movie.getCast()))
                            .productionCompanies(ProductionCompanyMapper.mapToProductionCompanyNames(movie.getProduction()))
                            .genres(GenreMapper.mapToGenreNames(movie.getGenres()))
                            .countries(CountryMapper.mapToCountryNames(movie.getCountries()))
                            .build();
    }

    public static MovieDetailsResponse mapToMovieDetailsResponse(Movie movie) {
        return MovieDetailsResponse.builder()
                                   .movieId(movie.getMovieId())
                                   .name(movie.getName())
                                   .releaseDate(movie.getReleaseDate())
                                   .duration(movie.getDuration())
                                   .description(movie.getDescription())
                                   .rating(movie.getRating())
                                   .reviewCount(movie.getReviewCount())
                                   .posterUrl(movie.getPosterUrl())
                                   .trailerUrl(movie.getTrailerUrl())
                                   .director(DirectorMapper.mapToDirectorDTO(movie.getDirector()))
                                   .cast(MovieCastMapper.mapToMovieCastDetailsResponse(movie.getCast()))
                                   .productionCompanies(ProductionCompanyMapper.mapToProductionCompanyDTOS(movie.getProduction()))
                                   .genres(GenreMapper.mapToGenreDTOS(movie.getGenres()))
                                   .countries(CountryMapper.mapToCountryDTOS(movie.getCountries()))
                                   .build();
    }

    public static MoviePage mapToMoviePage(Page<Movie> movies) {
        return MoviePage.builder()
                        .content(mapToMovieDetailsReponseList(movies))
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

    private static List<MovieDetailsResponse> mapToMovieDetailsReponseList(Page<Movie> movies) {
        return movies.stream()
                     .map(MovieMapper::mapToMovieDetailsResponse)
                     .toList();
    }

}
