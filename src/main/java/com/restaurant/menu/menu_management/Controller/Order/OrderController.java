package com.restaurant.menu.menu_management.Controller.Order;

import com.restaurant.menu.menu_management.Domain.Order;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDTO;
import com.restaurant.menu.menu_management.Service.Order.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Lấy tất cả Order dưới dạng DTO */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orderDTOs = orderService.fetchAllOrders();
        return ResponseEntity.ok(orderDTOs);
    }

    /** Lấy Order theo ID dưới dạng DTO */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") Long id) {
        OrderDTO orderDTO = this.orderService.fetchOrderByIdDTO(id);
        if (orderDTO != null) {
            return ResponseEntity.ok(orderDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Hoặc trả về thông báo lỗi
        }
    }

    /** Tạo Order mới */
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(order);
        return ResponseEntity.ok(new OrderDTO(newOrder));
    }

    /** Cập nhật Order */
    @PutMapping("/update")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        return ResponseEntity.ok(this.orderService.updateOrder(order));
    }

    /** Xóa Order */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {
        this.orderService.deleteOrder(id);
        return ResponseEntity.ok("Order và OrderDetail liên quan đã bị xóa.");
    }
}
