package com.moviehub.mapper.dto;

import com.moviehub.dto.PageableDTO;
import org.springframework.data.domain.Pageable;

public class PageableMapper {

    private PageableMapper() {
    }

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
