package com.epam.esm.repository.impl;

import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.model.Order;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order>
        implements OrderRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getAllByPageSorted(int start, int size) {
        String hql = "FROM " + entityClass.getName() + " s ORDER BY s.orderDate ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(start);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getAllOrdersForUserIdByPageSorted(int start, int size, long id) {
        String hql = "FROM " + entityClass.getName() + " s "
                + " WHERE s.user.id=:id ORDER BY s.orderDate ASC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id", id);
        query.setFirstResult(start);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public List<Order> getAllOrdersForUserId(long id) {
        String hql = "FROM " + entityClass.getName() + " s "
                + " WHERE s.user.id=:id ORDER BY s.orderDate ASC";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public long getCountOrdersForUserIdByPageSorted(int start, int size, long id) {
        String hql = "SELECT COUNT(*) FROM " + entityClass.getName() + " s "
                + " WHERE s.user.id=:id";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id", id);
        return (long) query.getSingleResult();
    }

    @Override
    public List<Order> getAllOrdersForSuperUser() {
        String hql = "SELECT s.user.id FROM " + entityClass.getName() + " s "
                + " GROUP BY s.user.id ORDER BY SUM(s.orderPrice) DESC";
        Query query = entityManager.createQuery(hql);
        query.setMaxResults(1);
        long superUserId = (long) query.getSingleResult();
        return getAllOrdersForUserId(superUserId);
    }
}