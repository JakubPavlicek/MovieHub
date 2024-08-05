package com.movie_manager.mapper.dto;

import com.movie_manager.dto.SortDTO;
import org.springframework.data.domain.Sort;

public class SortMapper {

    private SortMapper() {
    }

    public static SortDTO mapToSortDTO(Sort sort) {
        return SortDTO.builder()
                      .empty(sort.isEmpty())
                      .sorted(sort.isSorted())
                      .unsorted(sort.isUnsorted())
                      .build();
    }

}
