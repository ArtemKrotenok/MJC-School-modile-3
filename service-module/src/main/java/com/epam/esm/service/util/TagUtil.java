package com.epam.esm.service.util;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.model.TagDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TagUtil {

    private TagRepository tagRepository;

    public TagDTO convert(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public Tag convert(TagDTO tagDTO) {
        if (tagDTO == null) {
            return null;
        }
        Tag tagDB = null;
        if (tagDTO.getId() != null) {
            tagDB = tagRepository.findById(tagDTO.getId());
            if (tagDB != null) {
                return tagDB;
            }
        }

        tagDB = tagRepository.findByName(tagDTO.getName());
        if (tagDB != null) {
            return tagDB;
        }

        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        return tag;
    }
}