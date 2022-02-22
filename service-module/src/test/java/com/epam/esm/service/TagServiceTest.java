package com.epam.esm.service;

import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.imp.TagServiceImpl;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.model.TagDTO;
import com.epam.esm.service.util.TagUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.epam.esm.service.TestServiceDataUtil.*;
import static com.epam.esm.service.util.PaginationUtil.getStartPosition;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private static final int ONE_RESULT = 1;
    public static final int ITEMS_BY_PAGE = 10;
    public static final int START_PAGE = 1;
    @Mock
    private TagRepository tagRepository;
    private OrderRepository orderRepository;
    private TagService tagService;

    @BeforeEach
    public void setup() {
        TagUtil tagUtil = new TagUtil(tagRepository);
        this.tagService = new TagServiceImpl(tagRepository, orderRepository, tagUtil);
    }

    @Test
    void create_givenValidTag() {
        TagDTO tagDTO = TestServiceDataUtil.getValidTagDTO();
        tagDTO.setId(null);
        tagService.create(tagDTO);
    }

    @Test
    void create_givenInvalidTag_returnsCertificateServiceException() {
        TagDTO tagDTO = TestServiceDataUtil.getValidTagDTO();
        tagDTO.setName(null);
        CertificateServiceException exception = assertThrows(CertificateServiceException.class, () -> {
            tagService.create(tagDTO);
        });
        assertEquals(exception.getErrorResponseDTO().getErrorCode(),
                ResponseCode.NOT_VALID_INPUT_DATA.getCode());
    }

    @Test
    void findById_givenValidId_returnsTagDTO() {
        when(tagRepository.findById(TAG_TEST_ID)).thenReturn(TestServiceDataUtil.getValidTag());
        TagDTO finedTagDTO = tagService.findById(TAG_TEST_ID);
        assertEquals(finedTagDTO, TestServiceDataUtil.getValidTagDTO());
    }

    @Test
    void findById_givenInvalidId_returnsCertificateServiceException() {
        CertificateServiceException exception = assertThrows(CertificateServiceException.class, () -> {
            tagService.findById(TAG_TEST_ID_NOT_EXIST);
        });
        assertEquals(exception.getErrorResponseDTO().getErrorCode(),
                ResponseCode.NOT_FOUND.getCode());
    }

    @Test
    void deleteById_givenValidId() {
        Tag validTag = TestServiceDataUtil.getValidTag();
        when(tagRepository.findById(TAG_TEST_ID)).thenReturn(validTag);
        tagService.deleteById(TAG_TEST_ID);
    }

    @Test
    void deleteById_givenInvalidId_returnsCertificateServiceException() {
        when(tagRepository.findById(TAG_TEST_ID_NOT_EXIST)).thenReturn(null);
        CertificateServiceException exception = assertThrows(CertificateServiceException.class, () -> {
            tagService.deleteById(TAG_TEST_ID_NOT_EXIST);
        });
        assertEquals(exception.getErrorResponseDTO().getErrorCode(),
                ResponseCode.NOT_FOUND.getCode());
    }

    @Test
    void getAllByPageSorted_returnsSortedTagDTOs() {
        when(tagRepository.getAllByPageSorted(getStartPosition(START_PAGE, ITEMS_BY_PAGE), ITEMS_BY_PAGE)).
                thenReturn(TestServiceDataUtil.getValidTags(ITEMS_BY_PAGE));
        List<TagDTO> finedTagDTOList = tagService.getAllByPageSorted(START_PAGE, ITEMS_BY_PAGE);
        assertEquals(finedTagDTOList, TestServiceDataUtil.getValidTagDTOs(COUNT_TEST_TAG_LIST));
    }
}