package com.epam.esm.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SoldCertificateDTO {

    private Long id;
    private String soldPrice;
    private Integer count;
    private String discount;
    private CertificateDTO certificate;
}