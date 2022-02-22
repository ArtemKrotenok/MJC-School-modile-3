package com.epam.esm.service.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {

    private Long userId;
    private Long certificateId;
    private String orderDate;
    private String orderPrice;
}
