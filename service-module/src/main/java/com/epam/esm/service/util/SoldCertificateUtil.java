package com.epam.esm.service.util;

import com.epam.esm.repository.model.Certificate;
import com.epam.esm.repository.model.SoldCertificate;
import com.epam.esm.service.model.SoldCertificateCreateDTO;
import com.epam.esm.service.model.SoldCertificateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@AllArgsConstructor
public class SoldCertificateUtil {

    public static final int SCALE_PRICE = 2;
    public static final int SCALE_DISCOUNT = 2;
    private CertificateUtil certificateUtil;

    public SoldCertificateDTO convert(SoldCertificate entity) {
        if (entity == null) {
            return null;
        }
        return SoldCertificateDTO.builder()
                .id(entity.getId())
                .certificate(certificateUtil.convert(entity.getCertificate()))
                .soldPrice(entity.getSoldPrice().toString())
                .count(entity.getCount())
                .discount(entity.getDiscount().toString())
                .build();
    }

    public SoldCertificate convert(SoldCertificateCreateDTO dto) {
        Certificate certificateDB = certificateUtil.getCertificateDB(dto.getCertificateId());
        BigDecimal discount = new BigDecimal(dto.getDiscount()).setScale(SCALE_DISCOUNT, RoundingMode.CEILING);
        return SoldCertificate.builder()
                .certificate(certificateDB)
                .soldPrice(calculateSoldPrice(certificateDB.getPrice(), discount))
                .count(dto.getCount())
                .discount(discount)
                .build();
    }

    private BigDecimal calculateSoldPrice(BigDecimal price, BigDecimal discount) {
        return price.subtract((price.multiply(discount.divide(BigDecimal.valueOf(100),
                RoundingMode.CEILING)))).setScale(SCALE_PRICE, RoundingMode.CEILING);

    }
}