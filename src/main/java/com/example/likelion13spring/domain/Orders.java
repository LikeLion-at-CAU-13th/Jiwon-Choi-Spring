package com.example.likelion13spring.domain;

import com.example.likelion13spring.domain.Mapping.ProductOrders;
import com.example.likelion13spring.enums.DeliverStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

// 주문 엔티티 (BaseTimeEntity 상속)
// soft delete 적용을 위해 @SQLDelete 어노테이션 추가
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE orders SET deleted = true WHERE id = ?") // soft delete시 deleted=true 업데이트
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //주문 ID

    @Enumerated(EnumType.STRING)
    private DeliverStatus deliverStatus; // 배송상태

    // 구매자(Member)와 다대일 연관관계 매핑 (외래키 buyer_id)
    @ManyToOne
    @JoinColumn(name ="buyer_id")
    private Member buyer;

    // 주문과 상품주문(ProductOrders)의 일대다 연관관계 (양방향)
    // cascade ALL로 주문 저장시 상품주문도 함께 저장
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<ProductOrders> productOrders;

    // 쿠폰과 일대일 양방향 연관관계
    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private Coupon coupon;

    // setter: 배송정보 변경 가능하게
    // 배송정보 임베디드 타입으로 Orders 내에 포함
    @Setter
    @Embedded
    private ShippingAddress shippingAddress;

    // setter: soft delete 시 deleted 필드 변경
    // Soft delete용 flag, 기본값 false
    @Setter
    private Boolean deleted = false;
}
