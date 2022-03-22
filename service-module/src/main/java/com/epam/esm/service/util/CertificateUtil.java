package com.epam.esm.service.util;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.model.Certificate;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.CertificateDTO;
import com.epam.esm.service.model.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class CertificateUtil {

    private TagUtil tagUtil;
    private CertificateRepository certificateRepository;

    public CertificateDTO convert(Certificate certificate) {
        if (certificate == null) {
            return null;
        }
        CertificateDTO certificateDTO = CertificateDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice().toString())
                .createDate(DateUtil.convert(certificate.getCreateDate()))
                .lastUpdateDate(DateUtil.convert(certificate.getLastUpdateDate()))
                .build();
        if (certificate.getTags() != null) {
            certificateDTO.setTags(certificate.getTags().stream().map(tagUtil::convert).collect(Collectors.toSet()));
        }
        return certificateDTO;
    }

    public Certificate convert(CertificateDTO certificateDTO) {
        if (certificateDTO == null) {
            return null;
        }

        Certificate certificate = Certificate.builder()
                .name(certificateDTO.getName())
                .description(certificateDTO.getDescription())
                .duration(certificateDTO.getDuration())
                .price(new BigDecimal(certificateDTO.getPrice()))
                .tags(new HashSet<>())
                .build();

        if (certificateDTO.getTags() != null) {
            certificate.setTags(certificateDTO.getTags().stream().map(tagUtil::convert).collect(Collectors.toSet()));
        }
        if (certificateDTO.getCreateDate() == null) {
            certificate.setCreateDate(DateUtil.getNow());
        } else {
            certificate.setCreateDate(DateUtil.convert(certificateDTO.getCreateDate()));
        }
        if (certificateDTO.getLastUpdateDate() == null) {
            certificate.setLastUpdateDate(DateUtil.getNow());
        } else {
            certificate.setLastUpdateDate(DateUtil.convert(certificateDTO.getLastUpdateDate()));
        }
        return certificate;
    }

    public Certificate getCertificateDB(Long id) {
        Certificate certificateDB = certificateRepository.findById(id);
        if (certificateDB == null) {
            String errorMessage = ResponseCode.NOT_FOUND.getMessage() + " (certificate for id = " + id + ")";
            log.error(errorMessage);
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, errorMessage),
                    HttpStatus.NOT_FOUND);
        }
        return certificateDB;
    }
}