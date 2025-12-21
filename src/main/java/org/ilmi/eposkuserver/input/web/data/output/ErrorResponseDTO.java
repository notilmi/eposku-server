package org.ilmi.eposkuserver.input.web.data.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Data
@Builder
@NullMarked
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO<T> {
    private String code;
    private String message;
    @Nullable
    private T data;
}

