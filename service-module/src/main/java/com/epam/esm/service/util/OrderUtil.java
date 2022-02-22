package com.epam.esm.service.util;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.Certificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.exception.CertificateServiceException;
import com.epam.esm.service.model.OrderCreateDTO;
import com.epam.esm.service.model.OrderDTO;
import com.epam.esm.service.model.ResponseCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class OrderUtil {

    private CertificateUtil certificateUtil;
    private CertificateRepository certificateRepository;
    private UserRepository userRepository;
    private UserUtil userUtil;

    public OrderDTO convert(Order order) {
        if (order == null) {
            return null;
        }
        return OrderDTO.builder()
                .id(order.getId())
                .certificate(certificateUtil.convert(order.getCertificate()))
                .user(userUtil.convert(order.getUser()))
                .orderDate(DateUtil.convert(order.getOrderDate()))
                .orderPrice(order.getOrderPrice().toString())
                .build();
    }

    public Order convert(OrderCreateDTO dto) {
        Certificate certificateDB = certificateRepository.findById(dto.getCertificateId());
        if (certificateDB == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, "certificate for id=" + dto.getCertificateId()), HttpStatus.NOT_FOUND);
        }
        User userDB = userRepository.findById(dto.getUserId());
        if (userDB == null) {
            throw new CertificateServiceException(ResponseDTOUtil.getErrorResponseDTO(
                    ResponseCode.NOT_FOUND, "user for id=" + dto.getUserId()), HttpStatus.NOT_FOUND);
        }
        Order order = new Order();
        order.setUser(userDB);
        order.setCertificate(certificateDB);
        if (dto.getOrderPrice() == null || dto.getOrderPrice().equals("")) {
            order.setOrderPrice(certificateDB.getPrice());
        } else {
            order.setOrderPrice(new BigDecimal(dto.getOrderPrice()));
        }
        if (dto.getOrderDate() == null || dto.getOrderDate().equals("")) {
            order.setOrderDate(DateUtil.getNow());
        } else {
            order.setOrderDate(DateUtil.convert(dto.getOrderDate()));
        }
        return order;
    }
}