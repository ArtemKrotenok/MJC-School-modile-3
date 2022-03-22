package com.epam.esm.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "certificate")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    private Long duration;
    @Column(name = "create_date")
    private Timestamp createDate;
    @Column(name = "last_update_date")
    private Timestamp lastUpdateDate;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "certificate_tag",
            joinColumns = {@JoinColumn(name = "id_certificate")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")}
    )
    @ToString.Exclude
    Set<Tag> tags = new HashSet<>();

    @PrePersist
    public void onPrePersist() {
        this.createDate = Timestamp.valueOf(LocalDateTime.now());
        this.lastUpdateDate = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.lastUpdateDate = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(duration, that.duration) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate);
    }
}