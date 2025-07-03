package com.example.Jewelry.service;

import com.example.Jewelry.dto.request.ReviewRequest;
import com.example.Jewelry.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequest request, int userId);
    List<ReviewResponse> getReviewsByProductId(int productId);
    ReviewResponse updateReview(int reviewId, ReviewRequest request, int userId);
    void deleteReview(int reviewId, int userId);
    double getAverageRatingForProduct(int productId);
    int getTotalReviewsForProduct(int productId);
    ReviewResponse incrementHelpfulCount(int reviewId);
}
