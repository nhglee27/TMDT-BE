package com.example.Jewelry.dto.response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.Jewelry.entity.AuctionProduct;
import com.example.Jewelry.entity.AuctionRoom;
import com.example.Jewelry.entity.CTV;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReverseAuctionResponseDTO extends CommonApiResponse {

    @Getter
    @Setter
    @Builder
    public static class AuctionRoomDTO {
        private String roomID;

        private int auctionID;
        private int productID;
        /** Product INFO */
        private String productName;
        private Double startingPrice;
        private String productMaterial;
        private String productSize;
        private String occasion;
        private String description;
        private List<ImageDTO> productImages;

        /** Owner & Collaborator ID */
        private int authorID;
        private int collaboratorUserID;
        private String collaboratorName;

        /** Other stuff */
        private Double proposingPrice;
        private String status;
        private String statusCTV;
        private LocalDateTime createdAt;

        public static AuctionRoomDTO fromEntity(AuctionRoom auctionRoom) {
            if (auctionRoom == null) {
                return null;
            }

            // Lấy các đối tượng liên quan một cách an toàn (tránh NullPointerException)
            AuctionProduct auctionProduct = auctionRoom.getCurrentAuction();
            Product product = (auctionProduct != null) ? auctionProduct.getProduct() : null;
            User author = (auctionProduct != null) ? auctionProduct.getAuthor() : null;
            CTV collaborator = auctionRoom.getCollaborator();

            return AuctionRoomDTO.builder()
                    // Thông tin phòng
                    .roomID(auctionRoom.getId().toString())
                    .proposingPrice(auctionRoom.getProposingPrice())
                    .status(auctionRoom.getStatus())
                    .createdAt(auctionRoom.getCreatedAt())

                    // ID của các đối tượng liên quan
                    .auctionID(auctionProduct != null ? auctionProduct.getId() : 0)
                    .productID(product != null ? product.getId() : 0)
                    .authorID(author != null ? author.getId() : 0)
                    .collaboratorUserID(collaborator != null ? collaborator.getUser().getId() : 0)
                    .collaboratorName(collaborator != null ?
                    "%s %s".formatted(collaborator.getUser().getLastName(),collaborator.getUser().getFirstName()) : "noname")

                    // Thông tin chi tiết từ Product (lấy qua AuctionProduct)
                    .productName(product != null ? product.getName() : null)
                    .startingPrice(auctionProduct != null ? auctionProduct.getBudgetAuction() : null)
                    .productMaterial(product != null ? product.getProductMaterial() : null)
                    .productSize(product != null ? product.getSize() : null)
                    .occasion(product != null ? product.getOccasion() : null)
                    .description(product != null ? product.getDescription() : null)

                    // Chuyển đổi danh sách Image Entity sang danh sách ImageDTO
                    .productImages(
                            product != null && product.getImages() != null ? product.getImages().stream()
                                    .map(ImageDTO::fromEntity) // Giả định có phương thức fromEntity trong ImageDTO
                                    .collect(Collectors.toList())
                                    : Collections.emptyList())
                    .status(auctionRoom.getStatus())
                    .statusCTV(auctionRoom.getStatusCTV())
                    .build();
        }

    }

    /** For chat room list */
    private List<AuctionRoomDTO> roomList;
    /** For single */
    private AuctionRoomDTO room;

}
