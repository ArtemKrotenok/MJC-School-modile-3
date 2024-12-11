package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.model.UserDTO;
import com.epam.esm.service.util.ResponseDTOUtil;
import com.epam.esm.service.util.UserUtil;
import com.epam.esm.service.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.service.util.PaginationUtil.getStartPosition;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserUtil userUtil;

    @Override
    @Transactional
    public UserDTO findById(Long id) {
        ValidationUtil.validationId(id);
        User user = userRepository.findById(id);
        if (user != null) {
            log.info("user id= " + user.getId() + " find successfully");
            return userUtil.convert(user);
        }
        String errorMessage = ResponseCode.NOT_FOUND.getMessage() + " (for id = " + id + ")";
        log.error(errorMessage);
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND, errorMessage), HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public List<UserDTO> getAllByPageSorted(int page, int size) {
        ValidationUtil.validationPageSize(page, size);
        List<User> users = userRepository.getAllByPageSorted(getStartPosition(page, size), size);
        log.info(users.size() + " users find successfully");
        return convertResults(users);
    }

    @Override
    @Transactional
    public long getCount() {
        return userRepository.getCount();
    }

    private List<UserDTO> convertResults(List<User> users) {
        if (!users.isEmpty()) {
            return users.stream().map(userUtil::convert).collect(Collectors.toList());
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}