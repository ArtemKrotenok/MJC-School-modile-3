package com.epam.esm.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    T findById(I id);

    void add(T entity);

    void update(T entity);

    void delete(T entity);

    long getCount();

    List<T> getItemsByPage(int startPosition, int itemsByPage);

    void flush();
}
