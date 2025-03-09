package com.restaurant.menu.menu_management.Domain.DTO;

import com.restaurant.menu.menu_management.Domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {
    private Long orderId;
    private int rating;
    private String comment;
    private LocalDateTime reviewTime;

    public ReviewDTO(Review review) {
        this.orderId = review.getOrder().getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.reviewTime = review.getReviewTime();
    }
}
