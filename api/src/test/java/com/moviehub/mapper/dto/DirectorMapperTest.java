package com.moviehub.mapper.dto;

import com.moviehub.dto.AddDirectorRequest;
import com.moviehub.dto.DirectorDetailsResponse;
import com.moviehub.dto.DirectorPage;
import com.moviehub.dto.DirectorResponse;
import com.moviehub.dto.GenderName;
import com.moviehub.dto.UpdateDirectorRequest;
import com.moviehub.entity.Director;
import com.moviehub.entity.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DirectorMapperTest {
    
    private static final String FIRST_DIRECTOR_NAME = "Christopher Nolan";
    private static final String FIRST_DIRECTOR_BIO = "Famous director";
    private static final Gender FIRST_DIRECTOR_GENDER = Gender.MALE;

    private static final String SECOND_DIRECTOR_NAME = "Sofia Coppola";
    private static final String SECOND_DIRECTOR_BIO = "Another famous director";
    private static final Gender SECOND_DIRECTOR_GENDER = Gender.FEMALE;

    private static final UUID FIRST_DIRECTOR_UUID = UUID.fromString("30bb4ac5-3d69-4585-a563-408d4a02a414");
    private static final UUID SECOND_DIRECTOR_UUID = UUID.fromString("ff6c3338-229e-4816-925a-6f1f9324e01d");

    @Test
    void shouldMapToDirectorFromName() {
        Director director = DirectorMapper.mapToDirector(FIRST_DIRECTOR_NAME);

        assertThat(director).isNotNull();
        assertThat(director.getName()).isEqualTo(FIRST_DIRECTOR_NAME);
    }

    @Test
    void shouldReturnNullWhenMappingToDirectorFromNullName() {
        String nullName = null;
        Director director = DirectorMapper.mapToDirector(nullName);

        assertThat(director).isNull();
    }

    @Test
    void shouldMapToDirectorResponse() {
        Director director = createDirector(FIRST_DIRECTOR_UUID, FIRST_DIRECTOR_NAME, FIRST_DIRECTOR_BIO, FIRST_DIRECTOR_GENDER);

        DirectorResponse response = DirectorMapper.mapToDirectorResponse(director);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(FIRST_DIRECTOR_UUID);
        assertThat(response.getName()).isEqualTo(FIRST_DIRECTOR_NAME);
    }

    @Test
    void shouldMapToDirectorFromAddDirectorRequest() {
        AddDirectorRequest request = createAddDirectorRequest(FIRST_DIRECTOR_NAME, FIRST_DIRECTOR_BIO, GenderName.MALE);

        Director director = DirectorMapper.mapToDirector(request);

        assertThat(director).isNotNull();
        assertThat(director.getName()).isEqualTo(FIRST_DIRECTOR_NAME);
        assertThat(director.getBio()).isEqualTo(FIRST_DIRECTOR_BIO);
        assertThat(director.getGender()).isEqualTo(FIRST_DIRECTOR_GENDER);
    }

    @Test
    void shouldMapToDirectorFromUpdateDirectorRequest() {
        UpdateDirectorRequest request = createUpdateDirectorRequest(FIRST_DIRECTOR_NAME, FIRST_DIRECTOR_BIO, GenderName.MALE);

        Director director = DirectorMapper.mapToDirector(request);

        assertThat(director).isNotNull();
        assertThat(director.getName()).isEqualTo(FIRST_DIRECTOR_NAME);
        assertThat(director.getBio()).isEqualTo(FIRST_DIRECTOR_BIO);
        assertThat(director.getGender()).isEqualTo(FIRST_DIRECTOR_GENDER);
    }

    @Test
    void shouldMapToDirectorDetailsResponse() {
        Director director = createDirector(FIRST_DIRECTOR_UUID, FIRST_DIRECTOR_NAME, FIRST_DIRECTOR_BIO, FIRST_DIRECTOR_GENDER);

        DirectorDetailsResponse response = DirectorMapper.mapToDirectorDetailsResponse(director);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(FIRST_DIRECTOR_UUID);
        assertThat(response.getName()).isEqualTo(FIRST_DIRECTOR_NAME);
        assertThat(response.getBio()).isEqualTo(FIRST_DIRECTOR_BIO);
        assertThat(response.getGender()).isEqualTo(GenderName.MALE);
    }

    @Test
    void shouldMapToDirectorPage() {
        List<Director> directors = List.of(
            createDirector(FIRST_DIRECTOR_UUID, FIRST_DIRECTOR_NAME, FIRST_DIRECTOR_BIO, FIRST_DIRECTOR_GENDER),
            createDirector(SECOND_DIRECTOR_UUID, SECOND_DIRECTOR_NAME, SECOND_DIRECTOR_BIO, SECOND_DIRECTOR_GENDER)
        );
        Page<Director> directorPage = new PageImpl<>(directors, PageRequest.of(0, 2), 2);

        DirectorPage directorPageResponse = DirectorMapper.mapToDirectorPage(directorPage);

        assertThat(directorPageResponse).isNotNull();
        assertThat(directorPageResponse.getContent()).hasSize(2);
        assertThat(directorPageResponse.getContent().get(0).getName()).isEqualTo(FIRST_DIRECTOR_NAME);
        assertThat(directorPageResponse.getContent().get(1).getName()).isEqualTo(SECOND_DIRECTOR_NAME);
        assertThat(directorPageResponse.getTotalElements()).isEqualTo(2);
        assertThat(directorPageResponse.getTotalPages()).isEqualTo(1);
        assertThat(directorPageResponse.getFirst()).isTrue();
        assertThat(directorPageResponse.getLast()).isTrue();
    }

    private static Director createDirector(UUID id, String name, String bio, Gender gender) {
        return Director.builder()
                       .id(id)
                       .name(name)
                       .bio(bio)
                       .gender(gender)
                       .build();
    }
    
    private static AddDirectorRequest createAddDirectorRequest(String name, String bio, GenderName gender) {
        return AddDirectorRequest.builder()
                                 .name(name)
                                 .bio(bio)
                                 .gender(gender)
                                 .build();
    }

    private static UpdateDirectorRequest createUpdateDirectorRequest(String name, String bio, GenderName gender) {
        return UpdateDirectorRequest.builder()
                                    .name(name)
                                    .bio(bio)
                                    .gender(gender)
                                    .build();
    }
    
}