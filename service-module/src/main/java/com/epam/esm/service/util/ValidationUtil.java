package com.epam.esm.service.util;

import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.OrderCreateDTO;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.model.TagDTO;

public class ValidationUtil {

    public static void validationId(Long id) {
        if (id == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, "id cannot be null"));
        }
        if (id <= 0) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, "id must > 0"));
        }
    }

    public static void validationPageSize(Integer page, Integer size) {
        if (page == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, "page cannot be null"));
        }
        if (size == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, "size cannot be null"));
        }
        if (page <= 0) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, "page must > 0"));
        }
        if (size <= 0) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, "size must > 0"));
        }
    }

    public static void validationCreateDTO(TagDTO dto) {
        String errorMessage = null;
        if (dto.getName() == null || dto.getName().equals("")) {
            errorMessage = "tag name can't be empty";
        }
        if (dto.getId() != null) {
            errorMessage = "tag id should be null";
        }
        if (errorMessage != null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, errorMessage));
        }
    }

    public static void validationCreateDTO(OrderCreateDTO dto) {
        String errorMessage = null;
        if (dto.getUserId() == null) {
            errorMessage = "user id can't be empty";
        }
        if (dto.getCertificateId() == null) {
            errorMessage = "certificateId id should be null";
        }
        if (errorMessage != null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, errorMessage));
        }
    }
}