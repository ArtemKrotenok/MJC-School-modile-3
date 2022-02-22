package com.epam.esm.repository;

import com.epam.esm.repository.model.Certificate;

import java.util.List;

public interface CertificateRepository extends GenericRepository<Long, Certificate> {

    List<Certificate> search(int startPosition, int itemsByPage, String tag, String name, String description);

    List<Certificate> getAllByPageSorted(int startPosition, int itemsByPage);

    long getCountSearch(String tag, String name, String description);
}
