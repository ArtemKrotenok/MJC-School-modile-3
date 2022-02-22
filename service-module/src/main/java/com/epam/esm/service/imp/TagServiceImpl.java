package com.epam.esm.service.imp;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.service.util.PaginationUtil.getStartPosition;

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
        return tagUtil.convert(tag);
    }

    @Override
    @Transactional
    public TagDTO findById(long id) {
        ValidationUtil.validationId(id);
        Tag tag = tagRepository.findById(id);
        if (tag != null) {
            return tagUtil.convert(tag);
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND, "for id=" + id), HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        ValidationUtil.validationId(id);
        Tag tag = tagRepository.findById(id);
        if (tag == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, "for id=" + id), HttpStatus.NOT_FOUND);
        }
        try {
            tagRepository.delete(tag);
            tagRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_DELETE, "for id=" + id + " entity has dependencies"));
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
        for (Order order : orders) {
            for (Tag tag : order.getCertificate().getTags()) {
                if (countUseTag.containsKey(tag)) {
                    int count = countUseTag.get(tag);
                    count++;
                    countUseTag.put(tag, count);
                } else {
                    countUseTag.put(tag, 1);
                }
            }
        }
        Map.Entry<Tag, Integer> maxEntry = null;
        for (Map.Entry<Tag, Integer> entry : countUseTag.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        System.out.println(maxEntry.getValue());

        return tagUtil.convert(maxEntry.getKey());

    }

    private List<TagDTO> convertResults(List<Tag> tags) {
        if (!tags.isEmpty()) {
            return tags.stream().map(tagUtil::convert).collect(Collectors.toList());
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}