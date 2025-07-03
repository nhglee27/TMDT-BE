package com.example.Jewelry.controller;

import com.example.Jewelry.dto.request.CategoryRequestDTO;
import com.example.Jewelry.dto.response.CategoryResponseDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.resource.CategoryResource;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/category")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private final Logger LOG = LoggerFactory.getLogger(CategoryResource.class);

    @Autowired
    private CategoryResource categoryResource;

    @PostMapping("/add")
    @Operation(summary = "Api to add category")
    public ResponseEntity<CommonApiResponse> addCategory(
            @ModelAttribute CategoryRequestDTO request) {
        return categoryResource.addCategory(request);
    }

    @PutMapping(value = "/update/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Api to update category")
    public ResponseEntity<CommonApiResponse> updateCategory(@PathVariable("categoryId") int categoryId ,@ModelAttribute CategoryRequestDTO request) {
        LOG.info("Request received for update category, " + request.toString());
        return categoryResource.updateCategory(categoryId, request);
    }

    @GetMapping("/fetch/all")
    @Operation(summary = "Api to fetch all category")
    public ResponseEntity<CategoryResponseDTO> fetchAllCategory() {
        return categoryResource.fetchAllCategory();
    }

    @GetMapping("/fetch-deleted/all")
    @Operation(summary = "Api to fetch all category deleted true")
    public ResponseEntity<CategoryResponseDTO> fetchAllCategoryDeletedTrue() {
        return categoryResource.fetchAllCategoryDeleteTrue();
    }

    @PutMapping("/restore")
    @Operation(summary = "API to restock category")
    public ResponseEntity<CommonApiResponse> reStockCategory(@RequestParam("categoryId") int categoryId) {
        return categoryResource.reStoreCategory(categoryId);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Api to delete category all its events")
    public ResponseEntity<CommonApiResponse> deleteCategory(@RequestParam("categoryId") int categoryId) {
        return categoryResource.deleteCategory(categoryId);
    }

    @GetMapping(value = "/{categoryImageName}", produces = "image/*")
    public void fetchCourseImage(@PathVariable("categoryImageName") String categoryImageName, HttpServletResponse resp) {
        this.categoryResource.fetchCategoryImage(categoryImageName, resp);
    }

    @DeleteMapping("/delete-permanently")
    @Operation(summary = "Api to delete category all its events")
    public ResponseEntity<CommonApiResponse> deleteCategoryPermanently(@RequestParam("categoryId") int categoryId) {
        return categoryResource.deleteCategoryPermanently(categoryId);
    }

}

