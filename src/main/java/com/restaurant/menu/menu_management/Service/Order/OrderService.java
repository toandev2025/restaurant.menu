package com.restaurant.menu.menu_management.Service.Order;

import com.restaurant.menu.menu_management.Domain.*;
import com.restaurant.menu.menu_management.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
            DishRepository dishRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
    }

    /** Fetch tất cả Order và chuyển đổi sang OrderDTO */
    public List<OrderDTO> fetchAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderDTO::new).collect(Collectors.toList());
    }

    /** Fetch Order theo ID */
    public Order fetchOrderById(Long id) {
        Optional<Order> fetchOrder = this.orderRepository.findById(id);
        if (fetchOrder.isPresent()) {
            return fetchOrder.get();
        }
        return null;
    }

    @Transactional
    public Order createOrder(Order orderRequest) {
        // 🔹 Kiểm tra user có tồn tại không
        User user = this.userRepository.findById(orderRequest.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User ID không tồn tại"));

        // 🔹 Kiểm tra thông tin đơn hàng
        validateOrder(orderRequest);

        // 🔹 BƯỚC 1: Tạo Order trước
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderType(orderRequest.getOrderType());
        newOrder.setTableNumber(orderRequest.getTableNumber());
        newOrder.setLocation(orderRequest.getLocation());
        newOrder.setStatus(orderRequest.getStatus() != null ? orderRequest.getStatus() : "PENDING");

        newOrder = orderRepository.save(newOrder); // Lưu để lấy ID

        // 🔹 BƯỚC 2: Tạo OrderDetail từ danh sách món ăn
        double totalPrice = 0.0;
        for (OrderDetail od : orderRequest.getOrderDetails()) {
            Dish dish = dishRepository.findById(od.getDish().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish ID không tồn tại"));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setDish(dish);
            orderDetail.setQuantity(od.getQuantity());
            orderDetail.setPriceAtOrderTime(dish.getPrice() * od.getQuantity());

            orderDetailRepository.save(orderDetail);

            totalPrice += dish.getPrice() * od.getQuantity();
        }

        // 🔹 BƯỚC 3: Cập nhật tổng giá đơn hàng
        newOrder.setTotalPrice(totalPrice);
        return orderRepository.save(newOrder);
    }

    /** Cập nhật Order */
    @Transactional
    public Order updateOrder(Order updatedOrder) {
        Order existingOrder = fetchOrderById(updatedOrder.getId());

        // Cập nhật thông tin đơn hàng
        existingOrder.setOrderType(updatedOrder.getOrderType());
        existingOrder.setTableNumber(updatedOrder.getTableNumber());
        existingOrder.setLocation(updatedOrder.getLocation());

        // Xóa OrderDetail cũ
        orderDetailRepository.deleteAll(existingOrder.getOrderDetails());

        // Cập nhật lại danh sách OrderDetail mới
        double totalPrice = 0.0;
        for (OrderDetail od : updatedOrder.getOrderDetails()) {
            Dish dish = dishRepository.findById(od.getDish().getId()).orElseThrow();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(existingOrder);
            orderDetail.setDish(dish);
            orderDetail.setQuantity(od.getQuantity());
            orderDetail.setPriceAtOrderTime(dish.getPrice() * od.getQuantity());
            orderDetailRepository.save(orderDetail);

            totalPrice += dish.getPrice() * od.getQuantity();
        }

        existingOrder.setTotalPrice(totalPrice);
        return orderRepository.save(existingOrder);
    }

    /** Xóa Order */
    @Transactional
    public void deleteOrder(Long id) {
        Order existingOrder = fetchOrderById(id);
        orderDetailRepository.deleteAll(existingOrder.getOrderDetails()); // Xóa OrderDetail trước
        orderRepository.delete(existingOrder);
    }

    /** Kiểm tra tính hợp lệ của Order */
    private void validateOrder(Order order) {
        if (!order.getOrderType().equals("DINE_IN") && !order.getOrderType().equals("TAKEAWAY")) {
            throw new IllegalArgumentException("orderType phải là 'DINE_IN' hoặc 'TAKEAWAY'");
        }
        if (order.getOrderType().equals("DINE_IN") && order.getTableNumber() == null) {
            throw new IllegalArgumentException("TableNumber là bắt buộc khi orderType là 'DINE_IN'");
        }
        if (order.getOrderType().equals("TAKEAWAY") && (order.getLocation() == null || order.getLocation().isEmpty())) {
            throw new IllegalArgumentException("Location là bắt buộc khi orderType là 'TAKEAWAY'");
        }
    }
}
