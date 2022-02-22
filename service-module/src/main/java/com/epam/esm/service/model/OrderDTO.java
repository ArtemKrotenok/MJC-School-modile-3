package com.epam.esm.service.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends RepresentationModel<OrderDTO> {

    private Long id;
    private UserDTO user;
    private CertificateDTO certificate;
    private String orderDate;
    private String orderPrice;
}
