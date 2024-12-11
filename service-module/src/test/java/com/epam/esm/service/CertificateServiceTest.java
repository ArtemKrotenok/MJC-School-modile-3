package com.epam.esm.service;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Certificate;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.service.model.CertificateDTO;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.util.CertificateUtil;
import com.epam.esm.service.util.TagUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.epam.esm.service.TestServiceDataUtil.GIFT_CERTIFICATE_TEST_ID;
import static com.epam.esm.service.TestServiceDataUtil.GIFT_CERTIFICATE_TEST_ID_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private TagRepository tagRepository;
    private CertificateService certificateService;

    @BeforeEach
    public void setup() {
        TagUtil tagUtil = new TagUtil(tagRepository);
        CertificateUtil certificateUtil = new CertificateUtil(tagUtil, certificateRepository);
        this.certificateService = new CertificateServiceImpl(certificateRepository, certificateUtil, tagUtil);
    }

    @Test
    void create_givenValidCertificate() {
        CertificateDTO certificateDTO = TestServiceDataUtil.getValidCertificateDTO();
        certificateService.create(certificateDTO);
    }

    @Test
    void create_givenInvalidCertificate_returnsCertificateServiceException() {
        CertificateDTO certificateDTO = TestServiceDataUtil.getValidCertificateDTO();
        certificateDTO.setName(null);
        CertificateServiceException exception = assertThrows(CertificateServiceException.class, () -> {
            certificateService.create(certificateDTO);
        });
        assertEquals(exception.getErrorResponseDTO().getErrorCode(),
                ResponseCode.NOT_VALID_INPUT_DATA.getCode());
    }

    @Test
    void findById_givenValidId_returnsCertificateDTO() {
        when(certificateRepository.findById(GIFT_CERTIFICATE_TEST_ID)).thenReturn(TestServiceDataUtil.getValidCertificate());
        CertificateDTO finedCertificateDTO = certificateService.findById(GIFT_CERTIFICATE_TEST_ID);
        assertEquals(finedCertificateDTO, TestServiceDataUtil.getValidCertificateDTO());
    }

    @Test
    void findById_givenInvalidId_returnsCertificateServiceException() {
        CertificateServiceException exception = assertThrows(CertificateServiceException.class, () -> {
            certificateService.findById(GIFT_CERTIFICATE_TEST_ID_NOT_EXIST);
        });
        assertEquals(exception.getErrorResponseDTO().getErrorCode(),
                ResponseCode.NOT_FOUND.getCode());
    }

    @Test
    void deleteById_givenValidId() {
        Certificate validCertificate = TestServiceDataUtil.getValidCertificate();
        when(certificateRepository.findById(GIFT_CERTIFICATE_TEST_ID)).thenReturn(validCertificate);
        certificateService.deleteById(GIFT_CERTIFICATE_TEST_ID);
    }

    @Test
    void deleteById_givenInvalidId_returnsCertificateServiceException() {
        when(certificateRepository.findById(GIFT_CERTIFICATE_TEST_ID_NOT_EXIST)).thenReturn(null);
        CertificateServiceException exception = assertThrows(CertificateServiceException.class, () -> {
            certificateService.deleteById(GIFT_CERTIFICATE_TEST_ID_NOT_EXIST);
        });
        assertEquals(exception.getErrorResponseDTO().getErrorCode(),
                ResponseCode.NOT_FOUND.getCode());
    }

    @Test
    void update_givenValidCertificateDTO_returnsSuccessResponseDTO() {
        Certificate validCertificate = TestServiceDataUtil.getValidCertificate();
        CertificateDTO validCertificateDTO = TestServiceDataUtil.getValidCertificateDTO();
        when(certificateRepository.findById(validCertificate.getId())).thenReturn(validCertificate);
        certificateService.update(validCertificateDTO.getId(), validCertificateDTO);
    }
}