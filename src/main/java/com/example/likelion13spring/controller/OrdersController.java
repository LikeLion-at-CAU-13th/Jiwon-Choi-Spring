package com.example.likelion13spring.controller;

import com.example.likelion13spring.dto.request.OrdersCreateRequestDto;
import com.example.likelion13spring.dto.request.OrdersUpdateRequestDto;
import com.example.likelion13spring.dto.response.OrdersResponseDto;
import com.example.likelion13spring.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    // 주문 생성 API
    @PostMapping
    public ResponseEntity<OrdersResponseDto> createOrder(@RequestBody OrdersCreateRequestDto dto) {
        OrdersResponseDto responseDto = ordersService.createOrder(dto);
        return ResponseEntity.ok(responseDto);
    }

    // 구매자별 주문 목록 조회 API
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OrdersResponseDto>> getOrdersByBuyer(@PathVariable Long buyerId) {
        List<OrdersResponseDto> orderList = ordersService.getOrdersByBuyer(buyerId);
        return ResponseEntity.ok(orderList);
    }

    // 단건 주문 상세 조회 API
    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersResponseDto> getOrderDetail(@PathVariable Long orderId) {
        OrdersResponseDto order = ordersService.getOrderDetail(orderId);
        return ResponseEntity.ok(order);
    }

    // 배송정보 수정 API (배송 준비 상태일 때만 가능)
    @PutMapping("/{orderId}/address")
    public ResponseEntity<OrdersResponseDto> updateShippingInfo(@PathVariable Long orderId, @RequestBody OrdersUpdateRequestDto dto) {
        OrdersResponseDto updatedOrder = ordersService.updateShippingInfo(orderId, dto);
        return ResponseEntity.ok(updatedOrder);
    }

    // 주문 삭제 API (배송 완료 상태일 때만 soft delete 가능)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        ordersService.softDeleteOrder(orderId);
        return ResponseEntity.ok("주문이 성공적으로 삭제되었습니다.");
    }
}
