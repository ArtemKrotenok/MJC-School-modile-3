package com.epam.esm.app.util;

import com.epam.esm.app.controller.CertificateController;
import com.epam.esm.app.controller.OrderController;
import com.epam.esm.app.controller.UserController;
import com.epam.esm.service.model.OrderDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class LinkUtil {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_FIRST_PAGE = 1;

    public static void addLinksInfo(OrderDTO orderDTO) {
        orderDTO.getCertificate().add(linkTo(methodOn(CertificateController.class).getCertificateById(orderDTO.getCertificate().getId())).withSelfRel());
        orderDTO.getUser().add(linkTo(methodOn(UserController.class).getUserById(orderDTO.getUser().getId())).withSelfRel());
        orderDTO.add(linkTo(methodOn(OrderController.class).getOrderById(orderDTO.getId())).withSelfRel());
        orderDTO.getCertificate().getTags().forEach(tagDTO ->
                tagDTO.add(linkTo(methodOn(CertificateController.class).searchCertificates(DEFAULT_FIRST_PAGE, DEFAULT_PAGE_SIZE, tagDTO.getName(), "", "")).withRel("searchByTagName")));
    }

}
