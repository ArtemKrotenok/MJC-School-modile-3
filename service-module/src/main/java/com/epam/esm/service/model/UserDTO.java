package com.epam.esm.service.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends RepresentationModel<UserDTO> {

    private Long id;
    private String login;
    private String firstname;
    private String surname;

}
