package com.epam.esm.repository;

import com.epam.esm.repository.model.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    List<Order> getAllByPageSorted(int start, int size);

    List<Order> getAllOrdersForUserIdByPageSorted(int start, int size, long id);

    List<Order> getAllOrdersForUserId(long id);

    long getCountOrdersForUserIdByPageSorted(int start, int size, long id);

    List<Order> getAllOrdersForSuperUser();
}
