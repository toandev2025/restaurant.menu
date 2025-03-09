package com.restaurant.menu.menu_management.Domain.DTO;

import com.restaurant.menu.menu_management.Domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private UserDTO user;
    private Long orderId;
    private int rating;
    private String comment;
    private LocalDateTime reviewTime;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.orderId = review.getOrder().getId();
        this.user = new UserDTO(review.getUser());
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.reviewTime = review.getReviewTime();
    }
}
