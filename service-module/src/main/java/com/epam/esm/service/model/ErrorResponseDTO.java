package com.epam.esm.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {

    private String errorMessage;
    private Integer errorCode;
}
