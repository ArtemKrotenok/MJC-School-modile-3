package com.epam.esm.service.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {

    private Long userId;
    private List<SoldCertificateCreateDTO> soldCertificates;

}
