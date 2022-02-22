package com.epam.esm.service;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Certificate;
import com.epam.esm.service.model.CertificateDTO;
import com.epam.esm.service.util.CertificateUtil;
import com.epam.esm.service.util.TagUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateUtilTest {

    private CertificateUtil certificateUtil;
    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() {
        TagUtil tagUtil = new TagUtil(tagRepository);
        this.certificateUtil = new CertificateUtil(tagUtil);
    }

    @Test
    void convert_givenCertificateDTO_returnsCertificate() {
        CertificateDTO validCertificateDTO = TestServiceDataUtil.getValidCertificateDTO();
        Certificate validCertificate = TestServiceDataUtil.getValidCertificate();
        validCertificate.setId(null);
        when(tagRepository.findById(any(Long.class))).thenReturn(TestServiceDataUtil.getValidTag());
        Certificate covertCertificate = certificateUtil.convert(validCertificateDTO);
        assertThat(covertCertificate).isEqualTo(validCertificate);
    }

    @Test
    void convert_givenInvalidCertificateDTO_returnsNull() {
        CertificateDTO nullCertificateDTO = null;
        Certificate covertCertificateDTO = certificateUtil.convert(nullCertificateDTO);
        assertThat(covertCertificateDTO).isNull();
    }

    @Test
    void convert_givenCertificate_returnsCertificateDTO() {
        Certificate validCertificate = TestServiceDataUtil.getValidCertificate();
        CertificateDTO validCertificateDTO = TestServiceDataUtil.getValidCertificateDTO();
        CertificateDTO covertCertificateDTO = certificateUtil.convert(validCertificate);
        assertThat(covertCertificateDTO).isEqualTo(validCertificateDTO);
    }

    @Test
    void convert_givenInvalidCertificate_returnsNull() {
        Certificate nullCertificate = null;
        CertificateDTO covertCertificateDTO = certificateUtil.convert(nullCertificate);
        assertThat(covertCertificateDTO).isNull();
    }
}