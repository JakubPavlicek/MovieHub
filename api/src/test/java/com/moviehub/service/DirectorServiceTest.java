package com.moviehub.service;

import com.moviehub.entity.Director;
import com.moviehub.entity.Director_;
import com.moviehub.entity.Gender;
import com.moviehub.exception.DirectorAlreadyExistsException;
import com.moviehub.exception.DirectorNotFoundException;
import com.moviehub.repository.DirectorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectorServiceTest {

    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private DirectorService directorService;

    private static final UUID DIRECTOR_ID = UUID.fromString("9f50cdd6-be99-4477-8872-f71d97ab7fc2");
    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 10, Sort.by(Director_.NAME).ascending());

    private static final String FIRST_DIRECTOR_NAME = "Steven Spielberg";
    private static final String FIRST_DIRECTOR_BIO = "Bio 1";
    private static final Gender FIRST_DIRECTOR_GENDER = Gender.MALE;

    private static final String SECOND_DIRECTOR_NAME = "Kathryn Bigelow";
    private static final String SECOND_DIRECTOR_BIO = "Bio 2";
    private static final Gender SECOND_DIRECTOR_GENDER = Gender.FEMALE;

    @Test
    void shouldGetSavedDirectorWhenDirectorExists() {
        Director director = createDirector();

        when(directorRepository.findByName(director.getName())).thenReturn(Optional.of(director));

        Director savedDirector = directorService.getSavedDirector(director);

        assertThat(savedDirector.getName()).isEqualTo(director.getName());
    }

    @Test
    void shouldSaveDirectorWhenDirectorDoesNotExist() {
        Director director = createDirector();

        when(directorRepository.findByName(director.getName())).thenReturn(Optional.empty());
        when(directorRepository.save(director)).thenReturn(director);

        Director savedDirector = directorService.getSavedDirector(director);

        assertThat(savedDirector.getName()).isEqualTo(director.getName());
    }

    @Test
    void shouldReturnNullWhenDirectorIsNull() {
        assertThat(directorService.getSavedDirector(null)).isNull();
    }

    @Test
    void shouldAddDirectorIfDirectorDoesNotExist() {
        Director director = createDirector();

        when(directorRepository.existsByName(director.getName())).thenReturn(false);
        when(directorRepository.save(director)).thenReturn(director);

        Director addedDirector = directorService.addDirector(director);

        assertThat(addedDirector.getName()).isEqualTo(director.getName());
    }

    @Test
    void shouldThrowDirectorAlreadyExistsExceptionWhenDirectorAlreadyExists() {
        Director director = createDirector();

        when(directorRepository.existsByName(director.getName())).thenReturn(true);

        assertThatExceptionOfType(DirectorAlreadyExistsException.class)
            .isThrownBy(() -> directorService.addDirector(director));
    }

    @Test
    void shouldGetDirectorWhenDirectorExists() {
        Director director = createDirector();

        when(directorRepository.findById(director.getId())).thenReturn(Optional.of(director));

        Director savedDirector = directorService.getDirector(director.getId());

        assertThat(savedDirector.getName()).isEqualTo(director.getName());
    }

    @Test
    void shouldThrowDirectorNotFoundExceptionWhenDirectorDoesNotExist() {
        UUID directorId = UUID.fromString("5759868d-3063-42ec-95aa-ba259d9e52de");

        when(directorRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(DirectorNotFoundException.class)
            .isThrownBy(() -> directorService.getDirector(directorId));
    }

    private static Stream<Arguments> provideDirectorsForUpdateTest() {
        return Stream.of(
            Arguments.of(SECOND_DIRECTOR_NAME, SECOND_DIRECTOR_BIO, SECOND_DIRECTOR_GENDER, createDirector(SECOND_DIRECTOR_NAME, SECOND_DIRECTOR_BIO, SECOND_DIRECTOR_GENDER)),
            Arguments.of(null, SECOND_DIRECTOR_BIO, SECOND_DIRECTOR_GENDER, createDirector(FIRST_DIRECTOR_NAME, SECOND_DIRECTOR_BIO, SECOND_DIRECTOR_GENDER)),
            Arguments.of(SECOND_DIRECTOR_NAME, null, SECOND_DIRECTOR_GENDER, createDirector(SECOND_DIRECTOR_NAME, FIRST_DIRECTOR_BIO, SECOND_DIRECTOR_GENDER)),
            Arguments.of(SECOND_DIRECTOR_NAME, SECOND_DIRECTOR_BIO, null, createDirector(SECOND_DIRECTOR_NAME, SECOND_DIRECTOR_BIO, FIRST_DIRECTOR_GENDER))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDirectorsForUpdateTest")
    void shouldUpdateDirectorFields(String name, String bio, Gender gender, Director expectedDirector) {
        Director director = createDirector(FIRST_DIRECTOR_NAME, FIRST_DIRECTOR_BIO, FIRST_DIRECTOR_GENDER);

        when(directorRepository.findById(director.getId())).thenReturn(Optional.of(director));
        if (name != null) {
            when(directorRepository.existsByName(name)).thenReturn(false);
        }
        when(directorRepository.save(director)).thenReturn(expectedDirector);

        Director updatedDirector = directorService.updateDirector(director.getId(), createDirector(name, bio, gender));

        assertThat(updatedDirector.getName()).isEqualTo(expectedDirector.getName());
        assertThat(updatedDirector.getBio()).isEqualTo(expectedDirector.getBio());
        assertThat(updatedDirector.getGender()).isEqualTo(expectedDirector.getGender());
    }

    @Test
    void shouldGetDirectorsWithName() {
        Director director = createDirector();
        Page<Director> directorPage = new PageImpl<>(List.of(director), PAGE_REQUEST, 1L);

        when(directorRepository.findAllByName(director.getName(), PAGE_REQUEST)).thenReturn(directorPage);

        Page<Director> directors = directorService.getDirectors(PAGE_REQUEST.getPageNumber(), PAGE_REQUEST.getPageSize(), director.getName());

        assertThat(directors.getTotalElements()).isEqualTo(1);
        assertThat(directors.getContent()
                            .getFirst()
                            .getName()).isEqualTo(director.getName());
    }

    @Test
    void shouldGetDirectorsWhenNameIsEmpty() {
        String name = "";
        Director director = createDirector();
        Page<Director> directorPage = new PageImpl<>(List.of(director), PAGE_REQUEST, 1L);

        when(directorRepository.findAll(PAGE_REQUEST)).thenReturn(directorPage);

        Page<Director> directors = directorService.getDirectors(PAGE_REQUEST.getPageNumber(), PAGE_REQUEST.getPageSize(), name);

        assertThat(directors.getTotalElements()).isEqualTo(1);
        assertThat(directors.getContent()
                            .getFirst()
                            .getName()).isEqualTo(director.getName());
    }

    private static Director createDirector() {
        return Director.builder()
                       .id(DIRECTOR_ID)
                       .name(FIRST_DIRECTOR_NAME)
                       .build();
    }

    private static Director createDirector(String name, String bio, Gender gender) {
        return Director.builder()
                       .id(DIRECTOR_ID)
                       .name(name)
                       .bio(bio)
                       .gender(gender)
                       .build();
    }

}