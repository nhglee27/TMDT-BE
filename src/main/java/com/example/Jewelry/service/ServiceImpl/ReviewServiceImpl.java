package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.OrderDAO;
import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dao.ReviewDAO;
import com.example.Jewelry.dao.UserDAO;
import com.example.Jewelry.dto.request.ReviewRequest;
import com.example.Jewelry.dto.response.ReviewResponse;
import com.example.Jewelry.entity.Order;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.entity.Review;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public ReviewResponse addReview(ReviewRequest request, int userId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user"));
        Product product = productDAO.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));

        if (reviewDAO.existsByProductIdAndUserId(request.getProductId(), userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn đã đánh giá sản phẩm này rồi.");
        }
        if (!hasUserPurchasedProduct(userId, request.getProductId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only review products you have purchased.");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(request.getProductId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setHelpfulCount(0);

        Review savedReview = reviewDAO.save(review);
        return convertToResponse(savedReview, user.getUsername() != null ? user.getUsername() : user.getEmailId());
    }

    private boolean hasUserPurchasedProduct(int userId, int productId) {
        User user = userDAO.findById(userId).orElse(null);

        if (user == null || user.getEmailId() == null || user.getEmailId().isEmpty()) {
            return false;
        }

        return orderDAO.existsOrderWithProductByUserEmailAndStatus(
                user.getEmailId(),
                productId,
                Order.OrderStatus.PAID
        );
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(int productId) {
        productDAO.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
        List<Review> reviews = reviewDAO.findByProductId(productId);
        return reviews.stream()
                .map(review -> {
                    User user = userDAO.findById(review.getUserId()).orElse(null);
                    String username = (user != null && user.getUsername() != null) ? user.getUsername() : (user != null ? user.getEmailId() : "Anonymous");
                    return convertToResponse(review, username);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse updateReview(int reviewId, ReviewRequest request, int userId) {
        Review existingReview = reviewDAO.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        if (existingReview.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this review.");
        }

        existingReview.setRating(request.getRating());
        existingReview.setComment(request.getComment());
        // No updateAt field in entity, so not setting it here.

        Review updatedReview = reviewDAO.save(existingReview);
        User user = userDAO.findById(updatedReview.getUserId()).orElse(null);
        String username = (user != null && user.getUsername() != null) ? user.getUsername() : (user != null ? user.getEmailId() : "Anonymous");
        return convertToResponse(updatedReview, username);
    }

    @Override
    public void deleteReview(int reviewId, int userId) {
        Review existingReview = reviewDAO.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        if (existingReview.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this review.");
        }
        reviewDAO.delete(existingReview);
    }

    @Override
    public double getAverageRatingForProduct(int productId) {
        Double averageRating = reviewDAO.findAverageRatingByProductId(productId);

        return (averageRating == null) ? 0.0 : averageRating;
    }

    @Override
    public int getTotalReviewsForProduct(int productId) {
        List<Review> reviews = reviewDAO.findByProductId(productId);
        return reviews.size();
    }

    @Override
    public ReviewResponse incrementHelpfulCount(int reviewId) {
        Review review = reviewDAO.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        Review updatedReview = reviewDAO.save(review);
        User user = userDAO.findById(updatedReview.getUserId()).orElse(null);
        String username = (user != null && user.getUsername() != null) ? user.getUsername() : (user != null ? user.getEmailId() : "Anonymous");
        return convertToResponse(updatedReview, username);
    }

    private ReviewResponse convertToResponse(Review review, String username) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .username(username)
                .productId(review.getProductId())
                .rating(review.getRating())
                .comment(review.getComment())
                .helpfulCount(review.getHelpfulCount())
                .createdAt(review.getCreatedAt())
                .build();
    }
}