package com.epam.esm.repository;

import com.epam.esm.repository.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {

    List<User> getAllByPageSorted(int startPosition, int itemsByPage);

}
