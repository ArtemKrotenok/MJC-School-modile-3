package com.epam.esm.service.util;

import com.epam.esm.service.model.ErrorResponseDTO;
import com.epam.esm.service.model.ResponseCode;

public class ResponseDTOUtil {

    public static ErrorResponseDTO getErrorResponseDTO(ResponseCode responseCode) {
        return ErrorResponseDTO.builder()
                .errorMessage(responseCode.getMessage())
                .errorCode(responseCode.getCode())
                .build();
    }

    public static ErrorResponseDTO getErrorResponseDTO(ResponseCode responseCode, String description) {
        return ErrorResponseDTO.builder()
                .errorMessage(responseCode.getMessage() + " (" + description + ")")
                .errorCode(responseCode.getCode())
                .build();
    }

}