package com.epam.esm.service.util;

import com.epam.esm.repository.model.Certificate;
import com.epam.esm.service.model.CertificateDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CertificateUtil {

    private TagUtil tagUtil;

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
        Certificate certificate = new Certificate();
        certificate.setName(certificateDTO.getName());
        certificate.setDescription(certificateDTO.getDescription());
        certificate.setDuration(certificateDTO.getDuration());

        BigDecimal price = new BigDecimal(certificateDTO.getPrice());
        certificate.setPrice(price);
        if (certificateDTO.getTags() != null) {
            certificate.setTags(certificateDTO.getTags().stream().map(tagUtil::convert).collect(Collectors.toSet()));
        }
        if (certificateDTO.getCreateDate() == null) {
            certificate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        } else {
            certificate.setCreateDate(DateUtil.convert(certificateDTO.getCreateDate()));
        }
        if (certificateDTO.getLastUpdateDate() == null) {
            certificate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        } else {
            certificate.setLastUpdateDate(DateUtil.convert(certificateDTO.getLastUpdateDate()));
        }
        return certificate;
    }

}
