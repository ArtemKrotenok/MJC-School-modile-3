package com.epam.esm.service;

import com.epam.esm.service.model.OrderCreateDTO;
import com.epam.esm.service.model.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO findById(Long id);

    List<OrderDTO> getAllByPageSorted(int page, int size);

    long getCount();

    OrderDTO create(OrderCreateDTO orderCreateDTO);

    List<OrderDTO> getAllOrdersForUserIdByPageSorted(int page, int size, long id);

    long getCountOrdersForUserIdByPageSorted(int page, int size, long id);
}