package com.epam.esm.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sold_certificate")
public class SoldCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sold_price", nullable = false)
    private BigDecimal soldPrice;
    @Column(name = "count", nullable = false, columnDefinition = "integer default 1")
    private Integer count;
    @Column(name = "discount", nullable = false, columnDefinition = "integer default 0")
    private BigDecimal discount;
    @ManyToOne
    @JoinColumn(name = "id_certificate", nullable = false)
    private Certificate certificate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoldCertificate that = (SoldCertificate) o;
        return Objects.equals(id, that.id) && Objects.equals(soldPrice, that.soldPrice) && Objects.equals(count, that.count) && Objects.equals(discount, that.discount) && Objects.equals(certificate, that.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, soldPrice, count, discount, certificate);
    }
}