package com.moviehub.mapper.dto;

import com.moviehub.dto.PageableDTO;
import com.moviehub.dto.SortDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class PageableMapperTest {

    @Test
    void shouldMapPageableToPageableDTO() {
        Pageable pageable = PageRequest.of(2, 20, Sort.by("name").ascending());

        PageableDTO pageableDTO = PageableMapper.mapToPageableDTO(pageable);

        assertThat(pageableDTO.getPageNumber()).isEqualTo(2);
        assertThat(pageableDTO.getPageSize()).isEqualTo(20);
        assertThat(pageableDTO.getOffset()).isEqualTo(pageable.getOffset());
        assertThat(pageableDTO.getPaged()).isTrue();
        assertThat(pageableDTO.getUnpaged()).isFalse();

        SortDTO sortDTO = pageableDTO.getSort();
        assertThat(sortDTO.getEmpty()).isFalse();
        assertThat(sortDTO.getSorted()).isTrue();
    }

    @Test
    void shouldHandleEmptySortInPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

        PageableDTO pageableDTO = PageableMapper.mapToPageableDTO(pageable);

        assertThat(pageableDTO.getPageNumber()).isZero();
        assertThat(pageableDTO.getPageSize()).isEqualTo(10);
        assertThat(pageableDTO.getOffset()).isZero();
        assertThat(pageableDTO.getPaged()).isTrue();
        assertThat(pageableDTO.getUnpaged()).isFalse();

        SortDTO sortDTO = pageableDTO.getSort();
        assertThat(sortDTO.getEmpty()).isTrue();
        assertThat(sortDTO.getSorted()).isFalse();
    }

}