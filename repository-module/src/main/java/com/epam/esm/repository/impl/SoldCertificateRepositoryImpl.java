package com.epam.esm.repository.impl;

import com.epam.esm.repository.SoldCertificateRepository;
import com.epam.esm.repository.model.SoldCertificate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class SoldCertificateRepositoryImpl extends GenericRepositoryImpl<Long, SoldCertificate>
        implements SoldCertificateRepository {

}