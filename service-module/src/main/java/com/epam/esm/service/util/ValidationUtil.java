package com.epam.esm.service.util;

import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
        if (StringUtils.isBlank(dto.getName())) {
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
        if (dto.getSoldCertificates().isEmpty()) {
            errorMessage = "sold certificates list can't be empty";
        }
        dto.getSoldCertificates().forEach(ValidationUtil::validationCreateDTO);
        if (errorMessage != null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, errorMessage));
        }
    }

    public static void validationCreateDTO(SoldCertificateCreateDTO dto) {
        String errorMessage = null;
        if (dto.getCertificateId() == null) {
            errorMessage = "certificate id can't be empty";
        }
        if (dto.getCount() == null) {
            errorMessage = "count sold certificate can't be empty";
        }
        if (dto.getDiscount() == null) {
            errorMessage = "discount sold certificate can't be empty";
        }
        if (!isPositiveNumber(dto.getDiscount())) {
            errorMessage = "discount sold certificate is not valid";
        }
        if (errorMessage != null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, errorMessage));
        }
    }

    public static void validation(CertificateDTO certificateDTO) {
        String errorMessage = null;
        if (StringUtils.isBlank(certificateDTO.getName())) {
            errorMessage = "certificate name can't be empty";
        }
        if (StringUtils.isBlank(certificateDTO.getDescription())) {
            errorMessage = "certificate description can't be empty";
        }
        if (certificateDTO.getTags() == null || certificateDTO.getTags().isEmpty()) {
            errorMessage = "certificate tag list can't be empty";
        }
        if (!isPositiveNumber(certificateDTO.getPrice())) {
            errorMessage = "certificate price in not valid";
        }
        if (certificateDTO.getDuration() == null || certificateDTO.getDuration() <= 0) {
            errorMessage = "certificate duration should be > 0";
        }
        if (certificateDTO.getTags() == null || certificateDTO.getTags().isEmpty()) {
            errorMessage = "certificate tag list can't be empty";
        }
        if (errorMessage != null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, errorMessage));
        }
    }

    private static boolean isPositiveNumber(String str) {
        if (str == null) {
            return false;
        }
        return NumberUtils.toDouble(str) >= 0;
    }

}