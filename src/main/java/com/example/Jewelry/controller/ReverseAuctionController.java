package com.example.Jewelry.controller;

import com.example.Jewelry.dto.request.AddProductRequestDTO;
import com.example.Jewelry.dto.request.ReverseAuctionRequestDTO;
import com.example.Jewelry.dto.request.UpdateAuctionDetailDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.dto.response.ProductResponseDTO;
import com.example.Jewelry.dto.response.ReverseAuctionResponseDTO;
import com.example.Jewelry.resource.ProductResource;
import com.example.Jewelry.resource.UserResource;
import com.example.Jewelry.service.CategoryService;
import com.example.Jewelry.service.ProductService;
import com.example.Jewelry.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/reverse-auction")
public class ReverseAuctionController {

    private final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private ProductResource productResource;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @PostMapping("/add")
    @Operation(summary = "Api to add the Product Auction")
    public ResponseEntity<ProductResponseDTO> createProductAuction(@ModelAttribute AddProductRequestDTO request) {
        LOG.info("received request for adding the product Auction, " + request.toString());
        return productResource.createProductAuction(request);
    }

    @GetMapping("/fetch-all")
    @Operation(summary = "Api to fetch all product Auction")
    public ResponseEntity<ProductResponseDTO> fetchAllProductAuction() {
        return productResource.fetchAllProductAuction();
    }

    @GetMapping("/fetch-all/my/{userID}")
    @Operation(summary = "Api to fetch all product Auction By user")
    public ResponseEntity<ProductResponseDTO> fetchAllMyProductAuction(@PathVariable("userID") int userID) {
        return this.productResource.fetchAllMyProductAuction(userID);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse> createRegisterationForAuctionEntity(@RequestBody ReverseAuctionRequestDTO auctionRequestDTO) {       
        return productResource.createRegisterationForAuction(auctionRequestDTO);
    }

    @PostMapping("/reply-auction-request/{auctionRequestID}")
    public ResponseEntity<CommonApiResponse> responseToAuctionRequest(@PathVariable String auctionRequestID, @RequestBody String state) {
        boolean accepted = state.equalsIgnoreCase("accepted");
        return productResource.responseAuctionRequest(auctionRequestID, accepted);
    }

    /** Dựa trên ROLE mà nó sẽ trả về khác nhau */
    @GetMapping("/fetch-room/my/{userID}")
    public ResponseEntity<ReverseAuctionResponseDTO> getAvaliableRoomPerAuctionForAuthor(
        @PathVariable int userID,
        @RequestParam int productID
    ) {
        return productResource.getAllAuctionChatRoomPerAuction(userID, productID);
    }

    /** cập nhật giữa chừng */
    @PostMapping("/update-product")
    public ResponseEntity<ReverseAuctionResponseDTO> updateAuctionProduct(
        @RequestBody UpdateAuctionDetailDTO dto
    ) {
        return productResource.updateAuctionProduct(dto);
    }

}
