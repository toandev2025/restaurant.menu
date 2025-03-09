package com.restaurant.menu.menu_management.Service;

import com.restaurant.menu.menu_management.Domain.*;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDTO;
import com.restaurant.menu.menu_management.Domain.DTO.ReviewDTO;
import com.restaurant.menu.menu_management.Repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DishRepository dishRepository;
    private final ReviewService reviewService;
    private final AuthService authService;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
            DishRepository dishRepository, ReviewService reviewService, AuthService authService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.dishRepository = dishRepository;
        this.authService = authService;
        this.reviewService = reviewService;
    }

    /** Fetch tất cả Order và chuyển đổi sang OrderDTO */
    public List<OrderDTO> fetchAllOrders() {
        List<Order> orders = this.orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            ReviewDTO reviewDTO = this.reviewService.getReviewByOrderId(order.getId());
            OrderDTO orderDTO = new OrderDTO(order, reviewDTO);
            orderDTOs.add(orderDTO);
        }

        return orderDTOs;
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
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            ReviewDTO reviewDTO = this.reviewService.getReviewByOrderId(order.getId());
            return new OrderDTO(order, reviewDTO);
        }
        return null; // Trả về null nếu không tìm thấy
    }

    /** Create Order */
    @Transactional
    public Order createOrder(Order orderRequest) {

        User user = this.authService.getAuthenticatedUser();

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
            orderDetail.setNote(od.getNote());

            this.orderDetailRepository.save(orderDetail);

            totalAmount += orderDetail.getSubtotal();
        }

        newOrder.setTotalAmount(totalAmount);
        return this.orderRepository.save(newOrder);
    }

    /** Update Order */
    @Transactional
    public Order updateOrder(Order orderRequest) {

        Order existingOrder = fetchOrderById(orderRequest.getId());

        validateOrder(orderRequest); // Kiểm tra tính hợp lệ của orderRequest

        // Cập nhật thông tin Order (nếu có)
        existingOrder.setOrderType(orderRequest.getOrderType());
        existingOrder.setTableNumber(orderRequest.getTableNumber());
        existingOrder.setLocation(orderRequest.getLocation());
        existingOrder
                .setStatus(orderRequest.getStatus() != null ? orderRequest.getStatus() : existingOrder.getStatus());
        existingOrder.setPaymentMethod(orderRequest.getPaymentMethod());
        existingOrder.setDeliveryAddress(orderRequest.getDeliveryAddress());
        existingOrder.setPhoneNumber(orderRequest.getPhoneNumber());
        existingOrder.setNote(orderRequest.getNote());

        // Lấy danh sách OrderDetail hiện tại của Order
        List<OrderDetail> existingOrderDetails = orderDetailRepository.findByOrderId(existingOrder.getId());

        // Tạo một Map để kiểm tra OrderDetail nào cần cập nhật hoặc xóa
        Map<Long, OrderDetail> existingOrderDetailMap = existingOrderDetails.stream()
                .collect(Collectors.toMap(od -> od.getDish().getId(), od -> od));

        double totalAmount = 0.0;

        for (OrderDetail od : orderRequest.getOrderDetails()) {
            Dish dish = dishRepository.findById(od.getDish().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Dish ID không tồn tại"));

            if (existingOrderDetailMap.containsKey(dish.getId())) {
                // Nếu món đã có, chỉ cần cập nhật số lượng
                OrderDetail existingDetail = existingOrderDetailMap.get(dish.getId());
                existingDetail.setQuantity(od.getQuantity());
                existingDetail.calculateSubtotal();
                totalAmount += existingDetail.getSubtotal();
                orderDetailRepository.save(existingDetail);
                existingOrderDetailMap.remove(dish.getId()); // Đánh dấu là đã xử lý
            } else {
                // Nếu món chưa có, thêm mới OrderDetail
                OrderDetail newDetail = new OrderDetail();
                newDetail.setOrder(existingOrder);
                newDetail.setDish(dish);
                newDetail.setQuantity(od.getQuantity());
                newDetail.setUnitPrice(dish.getPrice());
                newDetail.calculateSubtotal();
                totalAmount += newDetail.getSubtotal();
                orderDetailRepository.save(newDetail);
            }
        }

        // Xóa OrderDetail không còn trong danh sách mới
        for (OrderDetail odToRemove : existingOrderDetailMap.values()) {
            orderDetailRepository.delete(odToRemove);
        }

        // Cập nhật tổng tiền đơn hàng
        existingOrder.setTotalAmount(totalAmount);
        return this.orderRepository.save(existingOrder);
    }

    /** Xóa Order */
    @Transactional
    public void deleteOrder(Long id) {
        Order existingOrder = fetchOrderById(id);
        this.orderDetailRepository.deleteAll(existingOrder.getOrderDetails());
        this.orderRepository.delete(existingOrder);
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
