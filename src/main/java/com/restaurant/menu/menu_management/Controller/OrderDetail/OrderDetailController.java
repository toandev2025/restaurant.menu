package com.restaurant.menu.menu_management.Controller.OrderDetail;

import com.restaurant.menu.menu_management.Domain.OrderDetail;
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

    // @GetMapping("/{orderId}")
    // public ResponseEntity<List<OrderDetail>>
    // getOrderDetailsByOrderId(@PathVariable("orderId") Long orderId) {
    // return
    // ResponseEntity.ok(this.orderDetailService.fetchOrderDetailsByOrderId(orderId));
    // }

    @GetMapping("/by-order/{id}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(this.orderDetailService.fetchOrderDetailsByOrderId(orderId));
    }

    // Chỉ api này hoạt động.
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.orderDetailService.fetchOrderDetailById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail orderDetail) {
        return ResponseEntity.ok(this.orderDetailService.createOrderDetail(orderDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable("id") Long id) {
        this.orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("Order Detail Deleted");
    }
}