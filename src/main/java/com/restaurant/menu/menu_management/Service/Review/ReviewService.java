package com.restaurant.menu.menu_management.Service.Review;

import com.restaurant.menu.menu_management.Domain.Review;
import com.restaurant.menu.menu_management.Domain.Order;
import com.restaurant.menu.menu_management.Domain.User;
import com.restaurant.menu.menu_management.Domain.DTO.UserDTO;
import com.restaurant.menu.menu_management.Repository.ReviewRepository;
import com.restaurant.menu.menu_management.Repository.OrderRepository;
import com.restaurant.menu.menu_management.Repository.UserRepository;
import com.restaurant.menu.menu_management.Service.AuthService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    private final AuthService authService;

    public ReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository, AuthService authService) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.authService = authService;
    }

    public Optional<Review> getReviewByOrderId(Long orderId) {
        return this.reviewRepository.findByOrderId(orderId);
    }

    @Transactional
    public Review createReview(Review reviewRequest) {
        User user = this.authService.getAuthenticatedUser(); // Lấy user từ JWT

        Order order = this.orderRepository.findById(reviewRequest.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Order không tồn tại"));

        if (!"COMPLETED".equals(order.getStatus())) {
            throw new IllegalArgumentException("Chỉ có thể đánh giá đơn hàng đã hoàn thành (COMPLETED)");
        }

        if (reviewRepository.findByOrderId(order.getId()).isPresent()) {
            throw new IllegalArgumentException("Order này đã có đánh giá trước đó");
        }

        Review newReview = new Review();
        newReview.setOrder(order);
        newReview.setUser(user); // User được lấy từ JWT
        newReview.setRating(reviewRequest.getRating());
        newReview.setComment(reviewRequest.getComment());
        return this.reviewRepository.save(newReview);
    }

    @Transactional
    public Review updateReview(Review reviewRequest) {
        Review existingReview = reviewRepository.findById(reviewRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Review không tồn tại"));

        existingReview.setRating(reviewRequest.getRating());
        existingReview.setComment(reviewRequest.getComment());
        return reviewRepository.save(existingReview);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
