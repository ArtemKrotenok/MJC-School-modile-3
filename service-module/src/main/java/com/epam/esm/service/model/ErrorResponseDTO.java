package com.epam.esm.service.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

    private String errorMessage;
    private Integer errorCode;
}
