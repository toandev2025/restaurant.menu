package com.restaurant.menu.menu_management.Service.OrderDetail;

import com.restaurant.menu.menu_management.Domain.OrderDetail;
import com.restaurant.menu.menu_management.Repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<OrderDetail> fetchOrderDetailsByOrderId(Long orderId) {
        return this.orderDetailRepository.findByOrderId(orderId);
    }

    public OrderDetail fetchOrderDetailById(Long id) {
        Optional<OrderDetail> orderDetail = this.orderDetailRepository.findById(id);
        return orderDetail.orElse(null);
    }

    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return this.orderDetailRepository.save(orderDetail);
    }

    public void deleteOrderDetail(Long id) {
        this.orderDetailRepository.deleteById(id);
    }
}
