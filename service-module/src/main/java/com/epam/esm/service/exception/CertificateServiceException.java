package com.epam.esm.service.exception;

import com.epam.esm.service.model.ErrorResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class CertificateServiceException extends RuntimeException {

    private ErrorResponseDTO errorResponseDTO;
    private HttpStatus responseHttpStatus = HttpStatus.BAD_REQUEST;

    public CertificateServiceException(ErrorResponseDTO errorResponseDTO) {
        this.errorResponseDTO = errorResponseDTO;
    }

    public CertificateServiceException(ErrorResponseDTO errorResponseDTO, HttpStatus responseHttpStatus) {
        this.errorResponseDTO = errorResponseDTO;
        this.responseHttpStatus = responseHttpStatus;
    }
}