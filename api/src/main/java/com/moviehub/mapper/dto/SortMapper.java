package com.moviehub.mapper.dto;

import com.moviehub.dto.SortDTO;
import org.springframework.data.domain.Sort;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between Sort entities and SortDTOs.
public class SortMapper {

    // Private constructor to prevent instantiation.
    private SortMapper() {
    }

    /// Maps a Sort entity to a SortDTO.
    ///
    /// @param sort The Sort entity to map.
    /// @return The corresponding SortDTO.
    public static SortDTO mapToSortDTO(Sort sort) {
        return SortDTO.builder()
                      .empty(sort.isEmpty())
                      .sorted(sort.isSorted())
                      .unsorted(sort.isUnsorted())
                      .build();
    }

}
