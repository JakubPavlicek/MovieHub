package com.moviehub.mapper.dto;

import com.moviehub.dto.PageableDTO;
import org.springframework.data.domain.Pageable;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Mapper class for converting between Pageable entities and Data Transfer Objects (DTOs).
public class PageableMapper {

    // Private constructor to prevent instantiation.
    private PageableMapper() {
    }

    /// Maps a Pageable object to a PageableDTO.
    ///
    /// @param pageable The Pageable object to map.
    /// @return A PageableDTO containing pagination information.
    public static PageableDTO mapToPageableDTO(Pageable pageable) {
        return PageableDTO.builder()
                          .pageNumber(pageable.getPageNumber())
                          .pageSize(pageable.getPageSize())
                          .sort(SortMapper.mapToSortDTO(pageable.getSort()))
                          .offset(pageable.getOffset())
                          .paged(pageable.isPaged())
                          .unpaged(pageable.isUnpaged())
                          .build();
    }

}
