package com.epam.esm.app.controller;

import com.epam.esm.app.model.Page;
import com.epam.esm.app.model.PageModel;
import com.epam.esm.app.util.LinkUtil;
import com.epam.esm.app.util.PageUtil;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.model.OrderCreateDTO;
import com.epam.esm.service.model.OrderDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * controller class for <b>Order</b> (for gift certificate).
 *
 * @author Artem Krotenok
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor

public class OrderController {

    public OrderService orderService;

    /**
     * controller for create new order
     *
     * @param orderCreateDTO - object contain new order model
     * @return created OrderDTO
     */
    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        OrderDTO newOrderDTO = orderService.create(orderCreateDTO);
        LinkUtil.addLinksInfo(newOrderDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newOrderDTO);
    }

    /**
     * controller for getting Order by id
     *
     * @param id - id number Order in database
     * @return OrderDTO
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable(name = "id") Long id) {
        OrderDTO orderDTO = orderService.findById(id);
        LinkUtil.addLinksInfo(orderDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDTO);
    }

    /**
     * controller for getting a list of Order by page using pagination (list sorted by name Order)
     *
     * @param page - number page for return
     * @param size - number of items per page
     * @return list OrderDTO
     */
    @GetMapping
    public ResponseEntity<Object> getOrdersByPageSorted(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        List<OrderDTO> orderDTOs = orderService.getAllByPageSorted(page, size);
        orderDTOs.forEach(LinkUtil::addLinksInfo);
        PageModel<List<OrderDTO>> response = new PageModel<>(orderDTOs);
        long countOrders = orderService.getCount();
        Page pageInfo = PageUtil.getPageInfo(page, size, countOrders);
        response.setPage(pageInfo);
        if (page >= 2) {
            response.getLinks().add(linkTo(methodOn(OrderController.class).getOrdersByPageSorted(page - 1, size)).withRel("previousPage"));
        }
        if (page < pageInfo.getTotalPages()) {
            response.getLinks().add(linkTo(methodOn(OrderController.class).getOrdersByPageSorted(page + 1, size)).withRel("nextPage"));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}