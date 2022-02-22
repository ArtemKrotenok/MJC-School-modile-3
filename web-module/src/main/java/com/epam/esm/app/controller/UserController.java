package com.epam.esm.app.controller;

import com.epam.esm.app.model.Page;
import com.epam.esm.app.model.PageModel;
import com.epam.esm.app.util.LinkUtil;
import com.epam.esm.app.util.PageUtil;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.model.OrderDTO;
import com.epam.esm.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * controller class for <b>user</b> (for gift certificate).
 *
 * @author Artem Krotenok
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor

public class UserController {

    public UserService userService;
    public OrderService orderService;

    /**
     * controller for getting a list of user by page using pagination (list sorted by name user)
     *
     * @param page - number page for return
     * @param size - number of items per page
     * @return list UserDTO
     */
    @GetMapping
    public ResponseEntity<Object> getUsersByPageSorted(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        List<UserDTO> userDTOs = userService.getAllByPageSorted(page, size);
        userDTOs.forEach(userDTO -> userDTO.add(linkTo(methodOn(UserController.class).getUserById(userDTO.getId())).withSelfRel()));
        PageModel<List<UserDTO>> response = new PageModel<>(userDTOs);
        long countUsers = userService.getCount();
        Page pageInfo = PageUtil.getPageInfo(page, size, countUsers);
        response.setPage(pageInfo);
        if (page >= 2) {
            response.getLinks().add(linkTo(methodOn(UserController.class).getUsersByPageSorted(page - 1, size)).withRel("previousPage"));
        }
        if (page < pageInfo.getTotalPages()) {
            response.getLinks().add(linkTo(methodOn(UserController.class).getUsersByPageSorted(page + 1, size)).withRel("nextPage"));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * controller for getting user by id
     *
     * @param id - id number user in database
     * @return UserDTO
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "id") Long id) {
        UserDTO userDTO = userService.findById(id);
        userDTO.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }

    /**
     * controller for getting orders for user
     *
     * @param id - id number user in database
     * @return list OrderDTO
     */

    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<Object> getAllOrdersForUser(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        List<OrderDTO> orderDTOs = orderService.getAllOrdersForUserIdByPageSorted(page, size, id);
        orderDTOs.forEach(LinkUtil::addLinksInfo);
        PageModel<List<OrderDTO>> response = new PageModel<>(orderDTOs);
        long countOrders = orderService.getCountOrdersForUserIdByPageSorted(page, size, id);
        Page pageInfo = PageUtil.getPageInfo(page, size, countOrders);
        response.setPage(pageInfo);
        if (page >= 2) {
            response.getLinks().add(linkTo(methodOn(UserController.class).getAllOrdersForUser(id, page - 1, size)).withRel("previousPage"));
        }
        if (page < pageInfo.getTotalPages()) {
            response.getLinks().add(linkTo(methodOn(UserController.class).getAllOrdersForUser(id, page + 1, size)).withRel("nextPage"));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}