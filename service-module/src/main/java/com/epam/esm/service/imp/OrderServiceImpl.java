package com.epam.esm.service.imp;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.service.util.PaginationUtil.getStartPosition;

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
            return orderUtil.convert(order);
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND, "for id=" + id), HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public List<OrderDTO> getAllByPageSorted(int page, int size) {
        ValidationUtil.validationPageSize(page, size);
        List<Order> orders = orderRepository.getAllByPageSorted(getStartPosition(page, size), size);
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
        return orderUtil.convert(newOrder);
    }

    @Override
    @Transactional
    public List<OrderDTO> getAllOrdersForUserIdByPageSorted(int page, int size, long id) {
        ValidationUtil.validationPageSize(page, size);
        ValidationUtil.validationId(id);
        List<Order> orders = orderRepository.getAllOrdersForUserIdByPageSorted(getStartPosition(page, size), size, id);
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
