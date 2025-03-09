package com.restaurant.menu.menu_management.Controller.Review;

import com.restaurant.menu.menu_management.Domain.Review;
import com.restaurant.menu.menu_management.Domain.DTO.ReviewDTO;
import com.restaurant.menu.menu_management.Service.AuthService;
import com.restaurant.menu.menu_management.Service.Review.ReviewService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService, AuthService authService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Review> getReviewByOrderId(@PathVariable Long orderId) {
        Optional<Review> review = reviewService.getReviewByOrderId(orderId);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody Review review) {
        Review newReview = this.reviewService.createReview(review);
        return ResponseEntity.ok(new ReviewDTO(newReview));
    }

    @PutMapping("/update")
    public ResponseEntity<Review> updateReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.updateReview(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Đánh giá đã được xóa thành công!");
    }
}
