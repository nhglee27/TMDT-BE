package com.example.Jewelry.controller;

import com.example.Jewelry.dto.request.PromoteProductRequestDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.dto.response.ProductResponseDTO;
import com.example.Jewelry.resource.ProductResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auctions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminAuctionController {

    private final Logger LOG = LoggerFactory.getLogger(AdminAuctionController.class);
    private final ProductResource productResource;

    @GetMapping("/list-all")
    @Operation(summary = "Admin: Lấy tất cả sản phẩm đấu giá (có phân trang)")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ProductResponseDTO> getAllProductAuctions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        LOG.info("Admin request to fetch all auction products. Page: {}, Size: {}, Status: {}", page, size, status);
        return productResource.fetchAllProductAuctionsForAdmin(page, size, status);
    }

    @DeleteMapping("/{productId}/delete")
    @Operation(summary = "Admin: Vô hiệu hóa (xóa mềm) một sản phẩm đấu giá")
    @PreAuthorize("hasAuthority('Admin')") //  Sửa thành hasAuthority('Admin')
    public ResponseEntity<CommonApiResponse> deleteAuctionProduct(@PathVariable int productId) {
        LOG.info("Admin request to delete auction product with ID: {}", productId);
        return productResource.deleteProduct(productId);
    }

    @PostMapping("/{productId}/restore")
    @Operation(summary = "Admin: Khôi phục một sản phẩm đấu giá đã bị xóa")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<CommonApiResponse> restoreAuctionProduct(@PathVariable int productId) {
        LOG.info("Admin request to restore auction product with ID: {}", productId);
        return productResource.reStoreProduct(productId);
    }

    @PostMapping("/{productId}/promote")
    @Operation(summary = "Admin: Chuyển sản phẩm đấu giá thành sản phẩm chính thức")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> promoteToStoreProduct(
            @PathVariable int productId,
            @RequestBody PromoteProductRequestDTO request) {
        LOG.info("Admin request to promote auction product with ID: {} to a store product", productId);
        return productResource.promoteAuctionToStoreProduct(productId,request);
    }
    @GetMapping("/{auctionProductId}/details")
    @Operation(summary = "Admin: Lấy thông tin chi tiết của một phiên đấu giá")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> getAuctionDetailsForAdmin(@PathVariable int auctionProductId) {
        LOG.info("Admin request to get details for auction product with ID: {}", auctionProductId);
        return productResource.getAuctionDetailsForAdmin(auctionProductId);
    }
}
