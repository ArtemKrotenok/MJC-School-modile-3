package com.epam.esm.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SoldCertificateCreateDTO {

    private Long certificateId;
    private Integer count;
    private String discount;

}