package com.epam.esm.service.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
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
