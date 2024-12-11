package com.epam.esm.repository.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Certificate;
import com.epam.esm.repository.model.Tag;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@AllArgsConstructor
public class CertificateRepositoryImpl extends GenericRepositoryImpl<Long, Certificate> implements CertificateRepository {

    private TagRepository tagRepository;

    @Override
    @SuppressWarnings("unchecked")
    public List<Certificate> getAllByPageSorted(int startPosition, int itemsByPage) {
        String hql = "FROM " + entityClass.getName() + " gs ORDER BY gs.name ASC";
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsByPage);
        return query.getResultList();
    }

    @Override
    public long getCountSearch(String tag, String name, String description) {
        String hql;
        Query query;
        if (tag == null && name == null && description == null) {
            hql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName();
            query = entityManager.createQuery(hql);
        } else {
            hql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName() + " gc WHERE ";
            List<Tag> dbTags = new ArrayList<>();
            if (tag != null) {
                String[] tags;
                if (tag.contains(",")) {
                    tags = tag.split(",");
                    if (tags.length == 0) {
                        return 0L;
                    }
                } else {
                    tags = new String[]{tag};
                }
                Tag tagDB;
                for (int i = 0; i < tags.length; i++) {
                    tagDB = tagRepository.findByName(tags[i].trim());
                    if (tagDB == null) {
                        return 0L;
                    }
                    dbTags.add(tagDB);
                    hql = hql.concat(":tag" + i + " MEMBER OF gc.tags");
                    if (i + 1 < tags.length) {
                        hql = hql.concat(" AND ");
                    }
                }
                if ((name != null) || (description != null)) {
                    hql = hql.concat(" AND ");
                }
            }
            if (name != null) {
                hql = hql.concat(" gc.name LIKE :name ");
                if (description != null) {
                    hql = hql.concat(" AND ");
                }
            }
            if (description != null) {
                hql = hql.concat(" gc.description LIKE :description ");
            }
            query = entityManager.createQuery(hql);
            if (!dbTags.isEmpty()) {
                for (int i = 0; i < dbTags.size(); i++) {
                    query.setParameter("tag" + i, dbTags.get(i));
                }
            }
            if (name != null) {
                query.setParameter("name", name);
            }
            if (description != null) {
                query.setParameter("description", description);
            }
        }
        return (long) query.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Certificate> search(int startPosition, int itemsByPage, String tag, String name, String description) {
        String hql;
        Query query;
        if (tag == null && name == null && description == null) {
            hql = "FROM " + entityClass.getSimpleName();
            query = entityManager.createQuery(hql);
        } else {
            hql = "FROM " + entityClass.getSimpleName() + " gc WHERE ";
            List<Tag> dbTags = new ArrayList<>();
            if (tag != null) {
                String[] tags = null;
                if (tag.contains(",")) {
                    tags = tag.split(",");
                    if (tags.length == 0) {
                        return Collections.emptyList();
                    }
                } else {
                    tags = new String[]{tag};
                }
                Tag tagDB;
                for (int i = 0; i < tags.length; i++) {
                    tagDB = tagRepository.findByName(tags[i].trim());
                    if (tagDB == null) {
                        return Collections.emptyList();
                    }
                    dbTags.add(tagDB);
                    hql = hql.concat(":tag" + i + " MEMBER OF gc.tags");
                    if (i + 1 < tags.length) {
                        hql = hql.concat(" AND ");
                    }
                }
                if ((name != null) || (description != null)) {
                    hql = hql.concat(" AND ");
                }
            }
            if (name != null) {
                hql = hql.concat(" gc.name LIKE :name ");
                if (description != null) {
                    hql = hql.concat(" AND ");
                }
            }
            if (description != null) {
                hql = hql.concat(" gc.description LIKE :description ");
            }
            query = entityManager.createQuery(hql);
            if (!dbTags.isEmpty()) {
                for (int i = 0; i < dbTags.size(); i++) {
                    query.setParameter("tag" + i, dbTags.get(i));
                }
            }
            if (name != null) {
                query.setParameter("name", name);
            }
            if (description != null) {
                query.setParameter("description", description);
            }
        }
        query.setFirstResult(startPosition);
        query.setMaxResults(itemsByPage);
        return query.getResultList();
    }

}