package com.opencommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private UUID productUuid;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productImage;

    @Column(nullable = false)
    private BigDecimal productPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {

        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        createdAt = LocalDateTime.now();
    }
}