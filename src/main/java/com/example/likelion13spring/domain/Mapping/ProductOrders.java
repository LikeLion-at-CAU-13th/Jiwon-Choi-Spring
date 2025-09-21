package com.example.likelion13spring.domain.Mapping;

import com.example.likelion13spring.domain.Orders;
import com.example.likelion13spring.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    private Integer quantity; // 구매 수량
}
