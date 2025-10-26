package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "merchant")
@Builder
public class Merchant {

    @Id
    @Column(name = "merchant_id", updatable = false, nullable = false)
    private String merchantId;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "website", length = 250)
    private String website;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status")
    private Boolean status;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "card_number", length = 19)
    private String cardNumber;
}
