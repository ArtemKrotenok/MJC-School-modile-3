package com.epam.esm.service.imp;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.model.UserDTO;
import com.epam.esm.service.util.OrderUtil;
import com.epam.esm.service.util.ResponseDTOUtil;
import com.epam.esm.service.util.UserUtil;
import com.epam.esm.service.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.service.util.PaginationUtil.getStartPosition;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserUtil userUtil;
    private OrderUtil orderUtil;

    @Override
    @Transactional
    public UserDTO findById(Long id) {
        ValidationUtil.validationId(id);
        User user = userRepository.findById(id);
        if (user != null) {
            return userUtil.convert(user);
        }
        throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                ResponseCode.NOT_FOUND, "for id=" + id), HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public List<UserDTO> getAllByPageSorted(int page, int size) {
        ValidationUtil.validationPageSize(page, size);
        List<User> users = userRepository.getAllByPageSorted(getStartPosition(page, size), size);
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
