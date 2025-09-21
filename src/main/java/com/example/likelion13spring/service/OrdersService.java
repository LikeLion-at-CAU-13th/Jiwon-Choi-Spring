package com.example.likelion13spring.service;

import com.example.likelion13spring.domain.Mapping.ProductOrders;
import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.domain.Orders;
import com.example.likelion13spring.domain.Product;
import com.example.likelion13spring.domain.ShippingAddress;
import com.example.likelion13spring.dto.request.OrdersCreateRequestDto;
import com.example.likelion13spring.dto.request.OrdersUpdateRequestDto;
import com.example.likelion13spring.dto.response.OrdersResponseDto;
import com.example.likelion13spring.enums.DeliverStatus;
import com.example.likelion13spring.repository.MemberRepository;
import com.example.likelion13spring.repository.OrdersRepository;
import com.example.likelion13spring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 주문 생성: 주문자의 잔액 확인, 상품 재고 확인 후 차감, 주문 저장
    @Transactional
    public OrdersResponseDto createOrder(OrdersCreateRequestDto dto) {
        Member buyer = memberRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매자입니다."));

        // 배송정보 Embeddable 객체 생성
        ShippingAddress address = ShippingAddress.builder()
                .recipient(dto.getRecipient())
                .phoneNumber(dto.getPhoneNumber())
                .roadAddress(dto.getRoadAddress())
                .detailAddress(dto.getDetailAddress())
                .postCode(dto.getPostCode())
                .build();

        // 주문 상품 리스트 생성 및 재고 체크
        List<ProductOrders> productOrdersList = dto.getProducts().stream()
                .map(orderProduct -> {
                    Product product = productRepository.findById(orderProduct.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

                    // 재고 부족 체크
                    if (product.getStock() < orderProduct.getQuantity()) {
                        throw new IllegalArgumentException("상품의 재고가 부족합니다: " + product.getName());
                    }

                    product.reduceStock(orderProduct.getQuantity()); // 재고 차감

                    return ProductOrders.builder()
                            .product(product)
                            .quantity(orderProduct.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        int totalPrice = productOrdersList.stream()
                .mapToInt(po -> po.getProduct().getPrice() * po.getQuantity())
                .sum();

        // 구매자 계좌 잔액 확인 및 차감
        if (buyer.getDeposit() < totalPrice) {
            throw new IllegalArgumentException("구매자의 계좌 잔액이 부족합니다.");
        }
        buyer.useDeposit(totalPrice);

        // Orders 엔티티 빌드 (초기 배송상태는 PREPARATION)
        Orders order = Orders.builder()
                .buyer(buyer)
                .deliverStatus(DeliverStatus.PREPARATION)
                .shippingAddress(address)
                .productOrders(productOrdersList)
                .build();

        // 양방향 연관관계 설정: ProductOrders가 Orders를 참조하도록 연결
        productOrdersList.forEach(po -> po.setOrders(order));

        // 주문 저장
        Orders savedOrder = ordersRepository.save(order);

        // 저장된 주문을 DTO로 변환하여 반환
        return toDto(savedOrder);
    }

    // 구매자별 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrdersResponseDto> getOrdersByBuyer(Long buyerId) {
        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매자입니다."));

        List<Orders> ordersList = ordersRepository.findByBuyer(buyer);

        return ordersList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 개별 주문 상세 조회
    @Transactional(readOnly = true)
    public OrdersResponseDto getOrderDetail(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        return toDto(order);
    }

    // 배송정보 수정 (배송상태가 PREPARATION일 때만 가능)
    @Transactional
    public OrdersResponseDto updateShippingInfo(Long orderId, OrdersUpdateRequestDto dto) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getDeliverStatus() != DeliverStatus.PREPARATION) {
            throw new IllegalArgumentException("배송 준비 상태에서만 배송정보 수정이 가능합니다.");
        }

        ShippingAddress newAddress = ShippingAddress.builder()
                .recipient(dto.getRecipient())
                .phoneNumber(dto.getPhoneNumber())
                .roadAddress(dto.getRoadAddress())
                .detailAddress(dto.getDetailAddress())
                .postCode(dto.getPostCode())
                .build();

        order.setShippingAddress(newAddress);

        return toDto(order);
    }

    // 주문 삭제 (soft delete, 배송 완료(COMPLETED) 상태에서만 가능)
    @Transactional
    public void softDeleteOrder(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (order.getDeliverStatus() != DeliverStatus.COMPLETED) {
            throw new IllegalArgumentException("배송 완료 상태에서만 주문 삭제가 가능합니다.");
        }

        order.setDeleted(true);
        ordersRepository.save(order);
    }

    // Orders 엔티티를 OrdersResponseDto로 변환하는 메서드
    private OrdersResponseDto toDto(Orders order) {
        return OrdersResponseDto.builder()
                .id(order.getId())
                .deliverStatus(order.getDeliverStatus())
                .shippingAddress(OrdersResponseDto.ShippingAddressDto.builder()
                        .recipient(order.getShippingAddress().getRecipient())
                        .phoneNumber(order.getShippingAddress().getPhoneNumber())
                        .roadAddress(order.getShippingAddress().getRoadAddress())
                        .detailAddress(order.getShippingAddress().getDetailAddress())
                        .postCode(order.getShippingAddress().getPostCode())
                        .build())
                .products(order.getProductOrders().stream().map(po -> OrdersResponseDto.ProductInfo.builder()
                        .productId(po.getProduct().getId())
                        .name(po.getProduct().getName())
                        .quantity(po.getQuantity())
                        .build()).toList())
                .build();
    }
}
