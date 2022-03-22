package com.epam.esm.app.util;

import com.epam.esm.app.controller.CertificateController;
import com.epam.esm.app.controller.OrderController;
import com.epam.esm.app.controller.TagController;
import com.epam.esm.app.controller.UserController;
import com.epam.esm.service.model.CertificateDTO;
import com.epam.esm.service.model.OrderDTO;
import com.epam.esm.service.model.TagDTO;
import com.epam.esm.service.model.UserDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class LinkUtil {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_FIRST_PAGE = 1;

    public static void addLinksInfo(OrderDTO orderDTO) {
        addLinksInfo(orderDTO.getUser());
        orderDTO.getSoldCertificates().forEach(soldCertificateDTO ->
                LinkUtil.addLinksInfo(soldCertificateDTO.getCertificate()));
        orderDTO.add(linkTo(methodOn(OrderController.class).getOrderById(orderDTO.getId())).withSelfRel());
        orderDTO.add(linkTo(methodOn(OrderController.class).createOrder(null)).withRel("create"));
    }

    public static void addLinksInfo(CertificateDTO certificateDTO) {
        certificateDTO.add(linkTo(methodOn(CertificateController.class).getCertificateById(certificateDTO.getId())).withSelfRel());
        certificateDTO.add(linkTo(methodOn(CertificateController.class).createCertificate(null)).withRel("create"));
        certificateDTO.add(linkTo(methodOn(CertificateController.class).deleteCertificateById(certificateDTO.getId())).withRel("delete"));
        certificateDTO.add(linkTo(methodOn(CertificateController.class).updateCertificate(certificateDTO.getId(), null)).withRel("update"));
        certificateDTO.getTags().forEach(tagDTO ->
                tagDTO.add(linkTo(methodOn(CertificateController.class).searchCertificates(DEFAULT_FIRST_PAGE, DEFAULT_PAGE_SIZE, tagDTO.getName(), "", "")).withRel("searchCertificateByTag")));
    }

    public static void addLinksInfo(TagDTO tagDTO) {
        tagDTO.add(linkTo(methodOn(TagController.class).getTagById(tagDTO.getId())).withSelfRel());
        tagDTO.add(linkTo(methodOn(TagController.class).createTag(null)).withRel("create"));
        tagDTO.add(linkTo(methodOn(TagController.class).deleteTagById(tagDTO.getId())).withRel("delete"));
        tagDTO.add(linkTo(methodOn(CertificateController.class).searchCertificates(DEFAULT_FIRST_PAGE, DEFAULT_PAGE_SIZE, tagDTO.getName(), "", "")).withRel("searchCertificateByTag"));
    }

    public static void addLinksInfo(UserDTO userDTO) {
        userDTO.add(linkTo(methodOn(UserController.class).getUserById(userDTO.getId())).withSelfRel());
        userDTO.add(linkTo(methodOn(UserController.class).getAllOrdersForUser(DEFAULT_FIRST_PAGE, DEFAULT_PAGE_SIZE, userDTO.getId())).withRel("searchCertificateByUser"));
    }

}
