package com.epam.esm.service.impl;

import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.model.TagDTO;
import com.epam.esm.service.util.ResponseDTOUtil;
import com.epam.esm.service.util.TagUtil;
import com.epam.esm.service.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.service.util.PaginationUtil.getStartPosition;

@Slf4j
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    private OrderRepository orderRepository;
    private TagUtil tagUtil;

    @Override
    @Transactional
    public TagDTO create(TagDTO tagDTO) {
        ValidationUtil.validationCreateDTO(tagDTO);
        if (tagRepository.findByName(tagDTO.getName()) != null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_CREATE, "tag by name: " + tagDTO.getName() + " already exists"));
        }
        Tag tag = tagUtil.convert(tagDTO);
        tagRepository.add(tag);
        log.info("tag id = " + tag.getId() + " create successfully");
        return tagUtil.convert(tag);
    }

    @Override
    @Transactional
    public TagDTO findById(long id) {
        ValidationUtil.validationId(id);
        Tag tag = tagRepository.findById(id);
        if (tag != null) {
            log.info("tag id = " + tag.getId() + " find successfully");
            return tagUtil.convert(tag);
        }
        String errorMessage = ResponseCode.NOT_FOUND.getMessage() + " (for id = " + id + ")";
        log.error(errorMessage);
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND, errorMessage),
                HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        ValidationUtil.validationId(id);
        Tag tag = tagRepository.findById(id);
        if (tag == null) {
            String errorMessage = ResponseCode.NOT_FOUND.getMessage() + " (for id = " + id + ")";
            log.error(errorMessage);
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, errorMessage),
                    HttpStatus.NOT_FOUND);
        }
        try {
            tagRepository.delete(tag);
            tagRepository.flush();
            log.info("tag id = " + tag.getId() + " delete successfully");
        } catch (DataIntegrityViolationException e) {
            String errorMessage = ResponseCode.NOT_DELETE.getMessage() + " (for id = " + id + " entity has dependencies)";
            log.error(errorMessage);
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_DELETE, errorMessage));
        }
    }

    @Override
    @Transactional
    public long getCount() {
        return tagRepository.getCount();
    }

    @Override
    @Transactional
    public List<TagDTO> getAllByPageSorted(int page, int size) {
        ValidationUtil.validationPageSize(page, size);
        List<Tag> tags = tagRepository.getAllByPageSorted(getStartPosition(page, size), size);
        log.info(tags.size() + " tags find successfully");
        return convertResults(tags);
    }

    @Override
    public TagDTO findSuper() {
        List<Order> orders = orderRepository.getAllOrdersForSuperUser();
        if (orders.isEmpty()) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        Map<Tag, Integer> countUseTag = new HashMap<>();
        orders.forEach(order -> order.getSoldCertificates()
                .forEach(soldCertificate -> soldCertificate.getCertificate().getTags().forEach(tag -> {
                    if (countUseTag.containsKey(tag)) {
                        countUseTag.put(tag, countUseTag.get(tag) + 1);
                    } else {
                        countUseTag.put(tag, 1);
                    }
                }))
        );

        final Map.Entry<Tag, Integer>[] maxEntry = new Map.Entry[]{countUseTag.entrySet().stream()
                .findFirst().orElseThrow(() -> new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND),
                HttpStatus.NOT_FOUND))};

        countUseTag.entrySet().stream().parallel().forEach(entry -> {
            if (entry.getValue().compareTo(maxEntry[0].getValue()) > 0) {
                maxEntry[0] = entry;
            }
        });
        log.info("super tag id= " + maxEntry[0].getKey().getId() + " find successfully");
        return tagUtil.convert(maxEntry[0].getKey());
    }

    private List<TagDTO> convertResults(List<Tag> tags) {
        if (!tags.isEmpty()) {
            return tags.stream().map(tagUtil::convert).collect(Collectors.toList());
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}