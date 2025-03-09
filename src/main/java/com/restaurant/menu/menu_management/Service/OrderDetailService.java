package com.restaurant.menu.menu_management.Service;

import com.restaurant.menu.menu_management.Domain.Order;
import com.restaurant.menu.menu_management.Domain.OrderDetail;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDetailDTO;
import com.restaurant.menu.menu_management.Repository.OrderDetailRepository;
import com.restaurant.menu.menu_management.Repository.OrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    public OrderDetailDTO fetchOrderDetailsByOrderId(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        Order getOrder = this.orderRepository.getById(orderId);

        if (orderDetails.isEmpty()) {
            return null;
        }

        // Chuyển đổi dữ liệu sang DTO
        List<OrderDetailDTO.Item> items = orderDetails.stream().map(orderDetail -> {
            OrderDetailDTO.Item itemDto = new OrderDetailDTO.Item();
            itemDto.setOrderDetail_id(orderDetail.getId());

            OrderDetailDTO.Dish dishDto = new OrderDetailDTO.Dish();
            dishDto.setId(orderDetail.getDish().getId());
            dishDto.setName(orderDetail.getDish().getName());
            dishDto.setPrice(orderDetail.getDish().getPrice());
            dishDto.setImageUrl(orderDetail.getDish().getImageUrl());

            itemDto.setDish(dishDto);
            itemDto.setQuantity(orderDetail.getQuantity());
            itemDto.setUnitPrice(orderDetail.getUnitPrice());
            itemDto.setSubtotal(orderDetail.getSubtotal());
            itemDto.setNote(orderDetail.getNote());
            itemDto.setStatus(getOrder.getStatus());

            return itemDto;
        }).collect(Collectors.toList());

        // Tính tổng số lượng món và tổng tiền
        int totalItems = items.size();
        double totalAmount = items.stream().mapToDouble(OrderDetailDTO.Item::getSubtotal).sum();

        OrderDetailDTO.Summary summary = new OrderDetailDTO.Summary(totalItems, totalAmount);
        summary.setTotalItems(totalItems);
        summary.setTotalAmount(totalAmount);

        // Gán vào DTO tổng thể
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderId(orderId);
        orderDetailDTO.setItems(items);
        orderDetailDTO.setSummary(summary);

        return orderDetailDTO;
    }
}
