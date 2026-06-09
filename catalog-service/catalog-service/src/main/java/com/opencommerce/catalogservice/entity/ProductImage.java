package com.opencommerce.catalogservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_images")
public class ProductImage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, updatable = false)
        private UUID uuid;

        @Column(nullable = false, length = 1500)
        private String imageUrl;

        @Column(nullable = false)
        @Builder.Default
        private Boolean isPrimary = false;

        @Column(nullable = false)
        private LocalDateTime createdAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonBackReference
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @PrePersist
        public void onCreate() {
            if (uuid == null) {
                uuid = UUID.randomUUID();
            }
            createdAt = LocalDateTime.now();
        }
}
