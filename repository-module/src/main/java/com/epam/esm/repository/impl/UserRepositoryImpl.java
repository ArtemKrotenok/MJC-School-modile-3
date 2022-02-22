package com.epam.esm.repository.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.User;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllByPageSorted(int startPosition, int itemsByPage) {
        String hql = "FROM " + entityClass.getName() + " u ORDER BY u.surname ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsByPage);
        return query.getResultList();
    }
}