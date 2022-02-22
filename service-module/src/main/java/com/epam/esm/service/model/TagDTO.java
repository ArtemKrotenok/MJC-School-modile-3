package com.epam.esm.service.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO extends RepresentationModel<TagDTO> {

    private Long id;
    private String name;
}
