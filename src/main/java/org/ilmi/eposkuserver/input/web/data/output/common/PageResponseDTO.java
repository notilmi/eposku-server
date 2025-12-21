package org.ilmi.eposkuserver.input.web.data.output.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseDTO<T> {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private List<T> content;
}
