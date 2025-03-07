package com.restaurant.menu.menu_management.Service.Order;

import com.restaurant.menu.menu_management.Domain.*;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDTO;
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

    /** Fetch Order theo ID và chuyển đổi sang OrderDTO */
    public OrderDTO fetchOrderByIdDTO(Long id) {
        return orderRepository.findById(id)
                .map(OrderDTO::new) // Chuyển đổi Order sang OrderDTO nếu tồn tại
                .orElse(null); // Trả về null nếu không tìm thấy
    }

    /** Create Order */
    @Transactional
    public Order createOrder(Order orderRequest) {
        User user = userRepository.findById(orderRequest.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User ID không tồn tại"));

        validateOrder(orderRequest);

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderType(orderRequest.getOrderType());
        newOrder.setTableNumber(orderRequest.getTableNumber());
        newOrder.setTotalAmount(0.0);
        newOrder.setLocation(orderRequest.getLocation());
        newOrder.setStatus(orderRequest.getStatus() != null ? orderRequest.getStatus() : "PENDING");
        newOrder.setPaymentMethod(orderRequest.getPaymentMethod());
        newOrder.setDeliveryAddress(orderRequest.getDeliveryAddress());
        newOrder.setPhoneNumber(orderRequest.getPhoneNumber());
        newOrder.setNote(orderRequest.getNote());

        newOrder = this.orderRepository.save(newOrder);

        double totalAmount = 0.0;
        for (OrderDetail od : orderRequest.getOrderDetails()) {
            Dish dish = dishRepository.findById(od.getDish().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish ID không tồn tại"));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setDish(dish);
            orderDetail.setQuantity(od.getQuantity());
            orderDetail.setUnitPrice(dish.getPrice()); // Lưu giá tại thời điểm đặt hàng
            orderDetail.calculateSubtotal(); // Tính toán subtotal

            orderDetailRepository.save(orderDetail);

            totalAmount += orderDetail.getSubtotal();
        }

        newOrder.setTotalAmount(totalAmount);
        return orderRepository.save(newOrder);
    }

    @Transactional
    public Order updateOrder(Order updatedOrder) {
        // Lấy đơn hàng hiện tại từ cơ sở dữ liệu
        Order existingOrder = fetchOrderById(updatedOrder.getId());

        // Cập nhật thông tin chung của đơn hàng
        existingOrder.setOrderType(updatedOrder.getOrderType());
        existingOrder.setTableNumber(updatedOrder.getTableNumber());
        existingOrder.setLocation(updatedOrder.getLocation());
        existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
        existingOrder.setDeliveryAddress(updatedOrder.getDeliveryAddress());
        existingOrder.setPhoneNumber(updatedOrder.getPhoneNumber());
        existingOrder.setNote(updatedOrder.getNote());
        existingOrder.setStatus(updatedOrder.getStatus());

        // Xóa tất cả OrderDetail cũ liên quan đến đơn hàng này
        orderDetailRepository.deleteAll(existingOrder.getOrderDetails());

        // Cập nhật lại danh sách OrderDetail mới
        double totalAmount = 0.0;
        for (OrderDetail od : updatedOrder.getOrderDetails()) {
            Dish dish = dishRepository.findById(od.getDish().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish ID không tồn tại"));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(existingOrder);
            orderDetail.setDish(dish);
            orderDetail.setQuantity(od.getQuantity());
            orderDetail.setUnitPrice(dish.getPrice()); // Lưu giá tại thời điểm cập nhật
            orderDetail.calculateSubtotal(); // Tính toán subtotal

            orderDetailRepository.save(orderDetail);

            totalAmount += orderDetail.getSubtotal(); // Cộng dồn vào tổng giá trị đơn hàng
        }

        // Cập nhật tổng giá trị đơn hàng
        existingOrder.setTotalAmount(totalAmount);

        // Lưu và trả về đơn hàng đã cập nhật
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
