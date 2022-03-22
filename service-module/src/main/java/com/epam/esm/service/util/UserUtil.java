package com.epam.esm.service.util;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.ResponseCode;
import com.epam.esm.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class UserUtil {

    private UserRepository userRepository;

    public UserDTO convert(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .firstname(user.getFirstname())
                .surname(user.getSurname())
                .build();
    }

    public User convert(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        if (userDTO.getId() != null) {
            User userDB = userRepository.findById(userDTO.getId());
            if (userDB != null) {
                return userDB;
            }
        }
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstname(userDTO.getFirstname());
        user.setSurname(userDTO.getSurname());
        return user;
    }

    public User getDbUser(Long id) {
        User userDB = userRepository.findById(id);
        if (userDB == null) {
            String errorMessage = ResponseCode.NOT_FOUND.getMessage() + " (user for id = " + id + ")";
            log.error(errorMessage);
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, errorMessage),
                    HttpStatus.NOT_FOUND);
        }
        return userDB;
    }

}