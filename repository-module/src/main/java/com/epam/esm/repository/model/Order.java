package com.epam.esm.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_info")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;
    @Column(name = "order_price", nullable = false)
    private BigDecimal orderPrice;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "order_sold_certificate",
            joinColumns = {@JoinColumn(name = "id_order")},
            inverseJoinColumns = {@JoinColumn(name = "id_sold_certificate")}
    )
    @ToString.Exclude
    Set<SoldCertificate> soldCertificates = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(user, order.user) && Objects.equals(orderDate, order.orderDate) && Objects.equals(orderPrice, order.orderPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, orderDate, orderPrice);
    }
}