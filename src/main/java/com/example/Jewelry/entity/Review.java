package com.example.Jewelry.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int productId;
    private int userId;

    private int rating; // 1 - 5
    private String comment;

    private int helpfulCount = 0;

    private LocalDateTime createdAt;

}
