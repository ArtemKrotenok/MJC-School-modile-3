package com.epam.esm.service.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TagDTO extends RepresentationModel<TagDTO> {

    private Long id;
    private String name;
}
