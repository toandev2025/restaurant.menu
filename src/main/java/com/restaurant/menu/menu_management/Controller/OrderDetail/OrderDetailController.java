package com.restaurant.menu.menu_management.Controller.OrderDetail;

import com.restaurant.menu.menu_management.Domain.OrderDetail;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDetailDTO;
import com.restaurant.menu.menu_management.Service.OrderDetail.OrderDetailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/by-order/{id}")
    public ResponseEntity<OrderDetailDTO> getOrderDetailsByOrderId(@PathVariable("id") Long orderId) {
        OrderDetailDTO orderDetailDTO = orderDetailService.fetchOrderDetailsByOrderId(orderId);

        if (orderDetailDTO == null) {
            return ResponseEntity.status(404).build(); // Trả về 404 nếu không tìm thấy Order
        }

        return ResponseEntity.ok(orderDetailDTO);
    }
}