package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Tag;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TagRepositoryImpl extends GenericRepositoryImpl<Long, Tag> implements TagRepository {

    @Override
    public Tag findByName(String name) {
        String hql = "FROM " + entityClass.getSimpleName() + " t WHERE t.name=:name";
        Query query = entityManager.createQuery(hql);
        query.setParameter("name", name);
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return (Tag) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tag> getAllByPageSorted(int startPosition, int itemsByPage) {
        String hql = "FROM " + entityClass.getName() + " t ORDER BY t.name ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsByPage);
        return query.getResultList();
    }
}