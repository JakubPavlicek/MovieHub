package com.moviehub.mapper.dto;

import com.moviehub.dto.SortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class SortMapperTest {

    @Test
    void shouldMapEmptySortToSortDTO() {
        Sort sort = Sort.unsorted();

        SortDTO sortDTO = SortMapper.mapToSortDTO(sort);

        assertThat(sortDTO.getEmpty()).isTrue();
        assertThat(sortDTO.getSorted()).isFalse();
        assertThat(sortDTO.getUnsorted()).isTrue();
    }

    @Test
    void shouldMapSortedSortToSortDTO() {
        Sort sort = Sort.by("name").ascending();

        SortDTO sortDTO = SortMapper.mapToSortDTO(sort);

        assertThat(sortDTO.getEmpty()).isFalse();
        assertThat(sortDTO.getSorted()).isTrue();
        assertThat(sortDTO.getUnsorted()).isFalse();
    }

    @Test
    void shouldMapSortedSortByMultipleFieldsToSortDTO() {
        Sort sort = Sort.by("name").descending().and(Sort.by("age").ascending());

        SortDTO sortDTO = SortMapper.mapToSortDTO(sort);

        assertThat(sortDTO.getEmpty()).isFalse();
        assertThat(sortDTO.getSorted()).isTrue();
        assertThat(sortDTO.getUnsorted()).isFalse();
    }

}