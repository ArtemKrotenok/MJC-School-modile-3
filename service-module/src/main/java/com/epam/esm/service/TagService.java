package com.epam.esm.service;

import com.epam.esm.service.model.TagDTO;

import java.util.List;

public interface TagService {

    TagDTO create(TagDTO tagDTO);

    TagDTO findById(long id);

    void deleteById(long id);

    long getCount();

    List<TagDTO> getAllByPageSorted(int page, int size);

    TagDTO findSuper();
}