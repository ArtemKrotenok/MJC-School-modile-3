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

    public static ErrorResponseDTO getErrorResponseDTO(ResponseCode responseCode, String message) {
        return ErrorResponseDTO.builder()
                .errorMessage(message)
                .errorCode(responseCode.getCode())
                .build();
    }

}