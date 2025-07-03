package com.example.Jewelry.controller;

import com.example.Jewelry.dto.ProductDTO;
import com.example.Jewelry.dto.request.AddProductRequestDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.dto.response.ProductResponseDTO;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.resource.ProductResource;
import com.example.Jewelry.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    @Autowired
    private ProductResource productResource;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    @Operation(summary = "Api to add the Admin or CTV")
    public ResponseEntity<ProductResponseDTO> addCourse(@ModelAttribute AddProductRequestDTO request) {
        return this.productResource.addProduct(request);
    }

    @GetMapping("/fetch-all")
    @Operation(summary = "Api to fetch all product")
    public ResponseEntity<ProductResponseDTO> fetchAllProduct() {
        return this.productResource.fetchAllProduct();
    }

    @GetMapping("/fetch-by-category/{categoryName}")
    @Operation(summary = "Api to fetch all product by category")
    public ResponseEntity<ProductResponseDTO> fetchAllProductByCategory(@PathVariable("categoryName") String categoryName) {
        return this.productResource.fetchAllProductByCategory(categoryName);
    }

    @GetMapping(value = "/{productImageName}", produces = "image/*")
    public void fetchProductImage(@PathVariable("productImageName") String productImageName, HttpServletResponse resp) {
        this.productResource.fetchProductImage(productImageName, resp);
    }

    @GetMapping("/fetch-deleted/all")
    @Operation(summary = "Api to fetch all product deleted true")
    public List<Product> fetchAllCategoryDeletedTrue() {
        return productService.fetchAllProductDeleteTrue();
    }

    @PutMapping("/restore")
    @Operation(summary = "API to restock product")
    public ResponseEntity<CommonApiResponse> reStoreProduct(@RequestParam("productId") int productId) {
        return productResource.reStoreProduct(productId);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Api to delete product all its events")
    public ResponseEntity<CommonApiResponse> deleteProduct(@RequestParam("productId") int productId) {
        return this.productResource.deleteProduct(productId);
    }

    @DeleteMapping("/delete-permanently")
    @Operation(summary = "Api to delete product all its events")
    public ResponseEntity<CommonApiResponse> deleteProductPermanently(@RequestParam("productId") int productId) {
        return productResource.deleteProductPermanently(productId);
    }

    @PutMapping(value = "/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Api to update product")
    public ResponseEntity<CommonApiResponse> updateProduct(@PathVariable("productId") int productId ,@ModelAttribute ProductDTO request) {
        return productResource.updateProduct(productId, request);
    }
    @GetMapping("/list")
    @Operation(summary = "API to show product list for user")
    public ResponseEntity<List<ProductDTO>> showProductList() {
        return ResponseEntity.ok(productService.getActiveProductListForShop());
    }
    @GetMapping("/{id}")
    @Operation(summary = "API to get single product for list page")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        ProductDTO product = productService.getProductDetailsForUser(id);
        return product != null
                ? ResponseEntity.ok(product)
                : ResponseEntity.notFound().build();
    }

}
