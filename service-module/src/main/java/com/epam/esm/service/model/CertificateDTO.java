package com.epam.esm.service.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDTO extends RepresentationModel<CertificateDTO> {

    private Long id;
    private String name;
    private String description;
    private String price;
    private Long duration;
    private String createDate;
    private String lastUpdateDate;
    private Set<TagDTO> tags;
}
