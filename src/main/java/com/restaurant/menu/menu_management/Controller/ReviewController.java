package com.restaurant.menu.menu_management.Controller;

import com.restaurant.menu.menu_management.Domain.Review;
import com.restaurant.menu.menu_management.Domain.DTO.ReviewDTO;
import com.restaurant.menu.menu_management.Service.AuthService;
import com.restaurant.menu.menu_management.Service.ReviewService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService, AuthService authService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ReviewDTO> getReviewByOrderId(@PathVariable Long orderId) {
        ReviewDTO reviewDTO = this.reviewService.getReviewByOrderId(orderId);
        if (reviewDTO != null) {
            return ResponseEntity.ok(reviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody Review review) {
        Review newReview = this.reviewService.createReview(review);
        return ResponseEntity.ok(new ReviewDTO(newReview));
    }

    @PutMapping("/update")
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody Review review) {
        Review newReview = this.reviewService.updateReview(review);
        return ResponseEntity.ok(new ReviewDTO(newReview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Đánh giá đã được xóa thành công!");
    }
}
