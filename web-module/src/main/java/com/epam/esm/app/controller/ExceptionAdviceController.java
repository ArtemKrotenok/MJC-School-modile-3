package com.epam.esm.app.controller;

import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.util.ResponseDTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class ExceptionAdviceController {

    @ExceptionHandler({CertificateServiceException.class})
    public ResponseEntity<Object> handleException(CertificateServiceException exception) {
        log.error(exception + " : " + exception.getErrorResponseDTO().getErrorMessage());
        return ResponseEntity
                .status(exception.getResponseHttpStatus())
                .body(exception.getErrorResponseDTO());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleException(MissingServletRequestParameterException exception) {
        log.error(exception + " : " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTOUtil.getErrorResponseDTO(ResponseCode.NOT_VALID_INPUT_DATA, exception.getMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleException(HttpMessageNotReadableException exception) {
        log.error(exception + " : " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTOUtil.getErrorResponseDTO(ResponseCode.NOT_VALID_INPUT_DATA, "check body data is correct"));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException exception) {
        log.error(exception + " : " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTOUtil.getErrorResponseDTO(ResponseCode.NOT_VALID_INPUT_DATA, "check URL is correct"));
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleException(Throwable exception) {
        log.error(exception.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTOUtil.getErrorResponseDTO(ResponseCode.UNKNOWN, ResponseCode.UNKNOWN.getMessage()));
    }
}