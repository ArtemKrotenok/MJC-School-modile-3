package com.epam.esm.service;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.model.TagDTO;
import com.epam.esm.service.util.TagUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagUtilTest {

    private TagUtil tagUtil;
    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() {
        this.tagUtil = new TagUtil(tagRepository);
    }

    @Test
    void convert_givenTagDTO_returnsTag() {
        TagDTO validTagDTO = TestServiceDataUtil.getValidTagDTO();
        when(tagRepository.findById(validTagDTO.getId())).thenReturn(TestServiceDataUtil.getValidTag());
        Tag validTag = TestServiceDataUtil.getValidTag();
        Tag covertTag = tagUtil.convert(validTagDTO);
        assertThat(covertTag).isEqualTo(validTag);
    }

    @Test
    void convert_givenInvalidTagDTO_returnsNull() {
        TagDTO nullTagDTO = null;
        Tag covertTagDTO = tagUtil.convert(nullTagDTO);
        assertThat(covertTagDTO).isNull();
    }

    @Test
    void convert_givenTag_returnsTagDTO() {
        Tag validTag = TestServiceDataUtil.getValidTag();
        TagDTO validTagDTO = TestServiceDataUtil.getValidTagDTO();
        TagDTO covertTagDTO = tagUtil.convert(validTag);
        assertThat(covertTagDTO).isEqualTo(validTagDTO);
    }

    @Test
    void convert_givenInvalidTag_returnsNull() {
        Tag nullTag = null;
        TagDTO covertTagDTO = tagUtil.convert(nullTag);
        assertThat(covertTagDTO).isNull();
    }
}