package com.epam.esm.service;

import com.epam.esm.service.model.CertificateDTO;

import java.util.List;

public interface CertificateService {

    CertificateDTO create(CertificateDTO certificateDTO);

    void deleteById(Long id);

    CertificateDTO findById(Long id);

    void update(CertificateDTO certificateDTO);

    List<CertificateDTO> search(Integer page, Integer size, String tag, String name, String description);

    long getCountSearch(String tag, String name, String description);
}