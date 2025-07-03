package com.example.Jewelry.controller;
import com.example.Jewelry.dto.request.ReviewRequest; // Đã đổi từ AddReviewRequestDTO sang ReviewRequest
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.dto.response.ReviewResponse;
import com.example.Jewelry.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Add a new review for a product")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody ReviewRequest request,
                                               @RequestHeader("userId") int userId) {
        ReviewResponse newReview = reviewService.addReview(request, userId);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all reviews for a specific product")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@PathVariable("productId") int productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update an existing review")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable("reviewId") int reviewId,
                                                       @RequestBody ReviewRequest request,
                                                       @RequestHeader("userId") int userId) {
        ReviewResponse updatedReview = reviewService.updateReview(reviewId, request, userId);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete a review")
    public ResponseEntity<CommonApiResponse> deleteReview(@PathVariable("reviewId") int reviewId,
                                                          @RequestHeader("userId") int userId) {
        reviewService.deleteReview(reviewId, userId);
        return new ResponseEntity<>(new CommonApiResponse("Review deleted successfully",
                true), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/product/{productId}/average-rating")
    @Operation(summary = "Get average rating for a product")
    public ResponseEntity<Double> getAverageRating(@PathVariable("productId") int productId) {
        Double averageRating = reviewService.getAverageRatingForProduct(productId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/product/{productId}/total-reviews")
    @Operation(summary = "Get total number of reviews for a product")
    public ResponseEntity<Integer> getTotalReviews(@PathVariable("productId") int productId) {
        int totalReviews = reviewService.getTotalReviewsForProduct(productId);
        return ResponseEntity.ok(totalReviews);
    }

    @PutMapping("/{reviewId}/helpful")
    @Operation(summary = "Increment helpful count for a review")
    public ResponseEntity<ReviewResponse> incrementHelpfulCount(@PathVariable("reviewId") int reviewId) {
        ReviewResponse updatedReview = reviewService.incrementHelpfulCount(reviewId);
        return ResponseEntity.ok(updatedReview);
    }
}
