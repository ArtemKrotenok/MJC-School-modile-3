package com.epam.esm.service.impl;

import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.model.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.OrderCreateDTO;
import com.epam.esm.service.model.OrderDTO;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.util.OrderUtil;
import com.epam.esm.service.util.ResponseDTOUtil;
import com.epam.esm.service.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.service.util.PaginationUtil.getStartPosition;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderUtil orderUtil;

    @Override
    @Transactional
    public OrderDTO findById(Long id) {
        ValidationUtil.validationId(id);
        Order order = orderRepository.findById(id);
        if (order != null) {
            log.info("order id= " + order.getId() + " find successfully");
            return orderUtil.convert(order);
        }
        String errorMessage = ResponseCode.NOT_FOUND.getMessage() + " (for id = " + id + ")";
        log.error(errorMessage);
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND, errorMessage),
                HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public List<OrderDTO> getAllByPageSorted(int page, int size) {
        ValidationUtil.validationPageSize(page, size);
        List<Order> orders = orderRepository.getAllByPageSorted(getStartPosition(page, size), size);
        log.info(orders.size() + " orders find successfully");
        return convertResults(orders);
    }

    @Override
    @Transactional
    public long getCount() {
        return orderRepository.getCount();
    }

    @Override
    @Transactional
    public OrderDTO create(OrderCreateDTO orderCreateDTO) {
        ValidationUtil.validationCreateDTO(orderCreateDTO);
        Order newOrder = orderUtil.convert(orderCreateDTO);
        orderRepository.add(newOrder);
        log.info("order id = " + newOrder.getId() + " create successfully");
        return orderUtil.convert(newOrder);
    }

    @Override
    @Transactional
    public List<OrderDTO> getAllOrdersForUserIdByPageSorted(int page, int size, long id) {
        ValidationUtil.validationPageSize(page, size);
        ValidationUtil.validationId(id);
        List<Order> orders = orderRepository.getAllOrdersForUserIdByPageSorted(getStartPosition(page, size), size, id);
        log.info(orders.size() + " orders for user find successfully");
        return orders.stream().map(orderUtil::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public long getCountOrdersForUserIdByPageSorted(int page, int size, long id) {
        return orderRepository.getCountOrdersForUserIdByPageSorted(getStartPosition(page, size), size, id);
    }

    private List<OrderDTO> convertResults(List<Order> orders) {
        if (!orders.isEmpty()) {
            return orders.stream().map(orderUtil::convert).collect(Collectors.toList());
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}
