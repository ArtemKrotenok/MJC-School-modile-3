package com.epam.esm.service.imp;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.model.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.CertificateDTO;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.util.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private CertificateRepository certificateRepository;
    private CertificateUtil certificateUtil;
    private TagUtil tagUtil;

    @Override
    @Transactional
    public CertificateDTO create(CertificateDTO certificateDTO) {
        validation(certificateDTO);
        Certificate certificate = certificateUtil.convert(certificateDTO);
        certificateRepository.add(certificate);
        return certificateUtil.convert(certificate);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        validation(id);
        Certificate certificate = certificateRepository.findById(id);
        if (certificate == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, "for id=" + id), HttpStatus.NOT_FOUND);
        }
        certificateRepository.delete(certificate);
//            if (giftCertificateRepository.delete(giftCertificate) != RESULT_ONE_RECORD) {
//                throw new GiftCertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
//                        ResponseCode.NOT_DELETE));
//            }

    }

    @Override
    @Transactional
    public void update(CertificateDTO certificateDTO) {
        Long updateIdGiftCertificate = certificateDTO.getId();
        validation(updateIdGiftCertificate);
        Certificate certificate = certificateRepository.findById(updateIdGiftCertificate);
        if (certificate == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, "for id=" + updateIdGiftCertificate), HttpStatus.NOT_FOUND);
        }
        certificate = getEntityForUpdate(certificate, certificateDTO);
        certificateRepository.update(certificate);
    }

    @Override
    @Transactional
    public CertificateDTO findById(Long id) {
        validation(id);
        Certificate certificate = certificateRepository.findById(id);
        if (certificate != null) {
            return certificateUtil.convert(certificate);
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND, "for id=" + id), HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public List<CertificateDTO> search(Integer page, Integer size, String tag, String name, String description) {
        ValidationUtil.validationPageSize(page, size);
        if (tag.equals("")) {
            tag = null;
        }
        if (name.equals("")) {
            name = null;
        }
        if (description.equals("")) {
            description = null;
        }
        int startPosition = PaginationUtil.getStartPosition(page, size);
        List<Certificate> certificates = certificateRepository.search(startPosition, size, tag, name, description);
        return convertResults(certificates);
    }

    @Override
    public long getCountSearch(String tag, String name, String description) {
        if (tag.equals("")) {
            tag = null;
        }
        if (name.equals("")) {
            name = null;
        }
        if (description.equals("")) {
            description = null;
        }
        return certificateRepository.getCountSearch(tag, name, description);
    }

    private Certificate getEntityForUpdate(Certificate certificate, CertificateDTO certificateDTO) {
        String updateName = certificateDTO.getName();
        if (updateName != null && !updateName.equals("")) {
            certificate.setName(updateName);
        }
        String updateDescription = certificateDTO.getDescription();
        if (updateDescription != null && !updateDescription.equals("")) {
            certificate.setDescription(updateDescription);
        }
        String updatePrice = certificateDTO.getPrice();
        if (updatePrice != null && !updatePrice.equals("")) {
            certificate.setPrice(new BigDecimal(updatePrice));
        }
        Long updateDuration = certificateDTO.getDuration();
        if (updateDuration != null && updateDuration >= 0) {
            certificate.setDuration(updateDuration);
        }
        String updateCreateDate = certificateDTO.getCreateDate();
        if (updateCreateDate != null && !updateCreateDate.equals("")) {
            certificate.setCreateDate(DateUtil.convert(updateCreateDate));
        }
        String updateLastUpdateDate = certificateDTO.getLastUpdateDate();
        if (updateLastUpdateDate != null && !updateLastUpdateDate.equals("")) {
            certificate.setCreateDate(DateUtil.convert(updateLastUpdateDate));
        }
        if (certificateDTO.getTags() != null) {
            certificate.setTags(certificateDTO.getTags().stream().map(tagUtil::convert).collect(Collectors.toSet()));
        }
        return certificate;
    }

    private void validation(Long id) {
        String errorMessage = null;
        if (id == null) {
            errorMessage = "id can't be empty";
        } else {
            if (id <= 0) {
                errorMessage = "id must > 0";
            }
        }
        if (errorMessage != null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, errorMessage));
        }
    }

    private void validation(CertificateDTO certificateDTO) {
        StringBuilder errorMessage = new StringBuilder();

        if (certificateDTO.getName() == null || certificateDTO.getName().equals("")) {
            errorMessage.append("certificate name can't be empty");
        }
        if (certificateDTO.getDescription() == null || certificateDTO.getDescription().equals("")) {
            errorMessage.append("certificate description can't be empty");
        }
        if (certificateDTO.getTags() == null || certificateDTO.getTags().isEmpty()) {
            errorMessage.append("certificate tag list can't be empty");
        }
        if (certificateDTO.getPrice() == null) {
            errorMessage.append("certificate price can't be empty");
        }
        if (certificateDTO.getDuration() == null || certificateDTO.getDuration() <= 0) {
            errorMessage.append("certificate duration should be > 0");
        }
        if (!errorMessage.toString().isEmpty()) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_VALID_INPUT_DATA, errorMessage.toString()));
        }
    }

    private List<CertificateDTO> convertResults(List<Certificate> certificates) {
        if (!certificates.isEmpty()) {
            return certificates.stream().map(certificateUtil::convert).collect(Collectors.toList());
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}