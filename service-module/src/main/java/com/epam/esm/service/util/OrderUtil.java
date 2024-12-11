package com.epam.esm.service.util;

import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.SoldCertificate;
import com.epam.esm.service.model.OrderCreateDTO;
import com.epam.esm.service.model.OrderDTO;
import com.epam.esm.service.model.SoldCertificateCreateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class OrderUtil {

    private SoldCertificateUtil soldCertificateUtil;
    private UserUtil userUtil;

    public OrderDTO convert(Order order) {
        if (order == null) {
            return null;
        }
        return OrderDTO.builder()
                .id(order.getId())
                .soldCertificates(order.getSoldCertificates().stream()
                        .map(soldCertificateUtil::convert)
                        .collect(Collectors.toList()))
                .user(userUtil.convert(order.getUser()))
                .orderDate(DateUtil.convert(order.getOrderDate()))
                .orderPrice(order.getOrderPrice().toString())
                .build();
    }

    public Order convert(OrderCreateDTO dto) {
        Set<SoldCertificate> soldCertificates = createSoldCertificates(dto.getSoldCertificates());
        return Order.builder()
                .user(userUtil.getDbUser(dto.getUserId()))
                .soldCertificates(soldCertificates)
                .orderDate(DateUtil.getNow())
                .orderPrice(soldCertificates.stream()
                        .map(SoldCertificate::getSoldPrice)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO))
                .build();
    }

    private Set<SoldCertificate> createSoldCertificates(List<SoldCertificateCreateDTO> soldCertificates) {
        return soldCertificates.stream().map(soldCertificateUtil::convert).collect(Collectors.toSet());
    }
}