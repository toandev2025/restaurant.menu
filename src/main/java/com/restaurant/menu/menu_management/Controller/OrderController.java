package com.restaurant.menu.menu_management.Controller;

import com.restaurant.menu.menu_management.Domain.Order;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDTO;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDetailDTO;
import com.restaurant.menu.menu_management.Service.OrderDetailService;
import com.restaurant.menu.menu_management.Service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public OrderController(OrderService orderService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    /** Lấy tất cả Order dưới dạng DTO */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orderDTO = orderService.fetchAllOrders();
        return ResponseEntity.ok(orderDTO);
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
        Order newOrder = this.orderService.createOrder(order);
        OrderDetailDTO detailDTO = this.orderDetailService.fetchOrderDetailsByOrderId(newOrder.getId());
        return ResponseEntity.ok(new OrderDTO(newOrder, detailDTO));
    }

    /** Cập nhật Order */
    @PutMapping("/update")
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody Order order) {
        Order newOrder = this.orderService.updateOrder(order);
        OrderDetailDTO detailDTO = this.orderDetailService.fetchOrderDetailsByOrderId(newOrder.getId());
        return ResponseEntity.ok(new OrderDTO(newOrder, detailDTO));
    }

    /** Xóa Order */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {
        this.orderService.deleteOrder(id);
        return ResponseEntity.ok("Order và OrderDetail liên quan đã bị xóa.");
    }

    /** Cập nhật trạng thái Order (Nhận ID từ JSON) */
    @PutMapping("/update/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@RequestBody Map<String, String> request) {
        Long orderId = Long.parseLong(request.get("id"));
        String newStatus = request.get("status");
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(new OrderDTO(updatedOrder));
    }
}
