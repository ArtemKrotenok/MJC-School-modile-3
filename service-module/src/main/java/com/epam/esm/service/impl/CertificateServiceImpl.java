package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.model.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.model.CertificateDTO;
import com.epam.esm.service.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private CertificateRepository certificateRepository;
    private CertificateUtil certificateUtil;
    private TagUtil tagUtil;

    @Override
    @Transactional
    public CertificateDTO create(CertificateDTO certificateDTO) {
        ValidationUtil.validation(certificateDTO);
        Certificate certificate = certificateUtil.convert(certificateDTO);
        certificateRepository.add(certificate);
        log.info("certificate id = " + certificate.getId() + " create successfully");
        return certificateUtil.convert(certificate);
    }

    @Override
    @Transactional
    public CertificateDTO update(Long id, CertificateDTO certificateDTO) {
        ValidationUtil.validationId(id);
        Certificate certificate = certificateUtil.getCertificateDB(id);
        getEntityForUpdate(certificate, certificateDTO);
        certificateRepository.update(certificate);
        log.info("certificate id = " + certificate.getId() + " update successfully");
        return certificateUtil.convert(certificate);
    }

    @Override
    @Transactional
    public CertificateDTO updateFull(Long id, CertificateDTO certificateDTO) {
        ValidationUtil.validationId(id);
        ValidationUtil.validation(certificateDTO);
        Certificate certificate = certificateUtil.getCertificateDB(id);
        setEntityForFullUpdate(certificate, certificateUtil.convert(certificateDTO));
        certificateRepository.update(certificate);
        log.info("certificate id = " + certificate.getId() + " update successfully");
        return certificateUtil.convert(certificate);
    }

    private void setEntityForFullUpdate(Certificate certificate, Certificate update) {
        certificate.setName(update.getName());
        certificate.setDescription(update.getDescription());
        certificate.setPrice(update.getPrice());
        certificate.setDuration(update.getDuration());
        certificate.setCreateDate(update.getCreateDate());
        certificate.setLastUpdateDate(update.getLastUpdateDate());
        certificate.setTags(update.getTags());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ValidationUtil.validationId(id);
        Certificate certificate = certificateUtil.getCertificateDB(id);
        certificateRepository.delete(certificate);
        log.info("certificate id = " + certificate.getId() + " delete successfully");
    }

    @Override
    @Transactional
    public CertificateDTO findById(Long id) {
        ValidationUtil.validationId(id);
        Certificate certificate = certificateUtil.getCertificateDB(id);
        log.info("certificate id = " + certificate.getId() + " find successfully");
        return certificateUtil.convert(certificate);
    }

    @Override
    @Transactional
    public List<CertificateDTO> search(Integer page, Integer size, String tag, String name, String description) {
        ValidationUtil.validationPageSize(page, size);
        if (StringUtils.isBlank(tag)) {
            tag = null;
        }
        if (StringUtils.isBlank(name)) {
            name = null;
        }
        if (StringUtils.isBlank(description)) {
            description = null;
        }
        int startPosition = PaginationUtil.getStartPosition(page, size);
        List<Certificate> certificates = certificateRepository.search(startPosition, size, tag, name, description);
        log.info(certificates.size() + " certificates find successfully");
        return convertResults(certificates);
    }

    @Override
    public long getCountSearch(String tag, String name, String description) {
        if (StringUtils.isBlank(tag)) {
            tag = null;
        }
        if (StringUtils.isBlank(name)) {
            name = null;
        }
        if (StringUtils.isBlank(description)) {
            description = null;
        }
        return certificateRepository.getCountSearch(tag, name, description);
    }

    private void getEntityForUpdate(Certificate certificate, CertificateDTO dto) {
        String updateName = dto.getName();
        if (!StringUtils.isBlank(updateName)) {
            certificate.setName(updateName);
        }
        String updateDescription = dto.getDescription();
        if (!StringUtils.isBlank(updateDescription)) {
            certificate.setDescription(updateDescription);
        }
        String updatePrice = dto.getPrice();
        if (!StringUtils.isBlank(updatePrice)) {
            certificate.setPrice(new BigDecimal(updatePrice));
        }
        Long updateDuration = dto.getDuration();
        if (updateDuration != null && updateDuration >= 0) {
            certificate.setDuration(updateDuration);
        }
        String updateCreateDate = dto.getCreateDate();
        if (!StringUtils.isBlank(updateCreateDate)) {
            certificate.setCreateDate(DateUtil.convert(updateCreateDate));
        }
        String updateLastUpdateDate = dto.getLastUpdateDate();
        if (!StringUtils.isBlank(updateLastUpdateDate)) {
            certificate.setCreateDate(DateUtil.convert(updateLastUpdateDate));
        }
        if (dto.getTags() != null) {
            certificate.setTags(dto.getTags().stream().map(tagUtil::convert).collect(Collectors.toSet()));
        }
    }

    private List<CertificateDTO> convertResults(List<Certificate> certificates) {
        return certificates.stream().map(certificateUtil::convert).collect(Collectors.toList());
    }
}