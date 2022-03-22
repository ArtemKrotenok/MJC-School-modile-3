package com.epam.esm.service.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class OrderDTO extends RepresentationModel<OrderDTO> {

    private Long id;
    private UserDTO user;
    private List<SoldCertificateDTO> soldCertificates;
    private String orderDate;
    private String orderPrice;
}
