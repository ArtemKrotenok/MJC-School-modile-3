package com.epam.esm.repository;

import com.epam.esm.repository.model.Tag;

import java.util.List;

public interface TagRepository extends GenericRepository<Long, Tag> {

    Tag findByName(String name);

    List<Tag> getAllByPageSorted(int startPosition, int itemsByPage);

}
