package com.epam.esm.service;

import com.epam.esm.service.model.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO findById(Long id);

    List<UserDTO> getAllByPageSorted(int page, int size);

    long getCount();
}