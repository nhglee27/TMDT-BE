package com.example.Jewelry.resource;

import com.example.Jewelry.Utility.Constant;
import com.example.Jewelry.dao.CategoryDAO;
import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dto.request.CategoryRequestDTO;
import com.example.Jewelry.dto.response.CategoryResponseDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.entity.Category;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.exception.CategorySaveFailedException;
import com.example.Jewelry.service.CategoryService;
import com.example.Jewelry.service.ProductService;
import com.example.Jewelry.service.StorageService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class CategoryResource {
    private final Logger LOG = LoggerFactory.getLogger(CategoryResource.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private StorageService storageService;


    public ResponseEntity<CommonApiResponse> addCategory(CategoryRequestDTO request) {
        LOG.info("Request received for add category");

        CommonApiResponse response = new CommonApiResponse();
        if (request == null) {
            response.setResponseMessage("missing input");
            response.setSuccess(false);

            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
        }
        // Kiểm tra xem category đã tồn tại chưa (theo name)
        boolean exists = categoryService.existsByName(request.getName().trim());
        if (exists) {
            response.setResponseMessage("Category already exists");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        LocalDateTime now = LocalDateTime.now();
        String thumbnailFilename = this.storageService.storeCategoryImage(request.getThumbnail());
        Category category = Category.builder()
                .name(request.getName())
                .thumbnail(thumbnailFilename)
                .status(request.getStatus())
                .deleted(false)
                .createdAt(now)
                .updateAt(now)
                .build();


        Category savedCategory = this.categoryService.addCategory(category);

        if (savedCategory == null) {
            throw new CategorySaveFailedException("Failed to add category");
        }

        response.setResponseMessage("Category Added Successful");
        response.setSuccess(true);

        return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
    }

    public ResponseEntity<CommonApiResponse> updateCategory(int categoryId, CategoryRequestDTO request) {
        LOG.info("Request received for update category");

        CommonApiResponse response = new CommonApiResponse();

        if (request == null) {
            response.setResponseMessage("Missing input");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (categoryId == 0) {
            response.setResponseMessage("Missing category ID");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem category có tồn tại không
        Category existingCategory = categoryService.getCategoryById(categoryId);
        if (existingCategory == null) {
            response.setResponseMessage("Category not found");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Cập nhật thông tin category
        String thumbnailFilename = this.storageService.storeCategoryImage(request.getThumbnail());
        existingCategory.setName(request.getName());
        existingCategory.setThumbnail(thumbnailFilename);
        existingCategory.setStatus(request.getStatus());
        existingCategory.setUpdateAt(LocalDateTime.now());

        // Lưu category đã cập nhật
        Category savedCategory = categoryService.updateCategory(existingCategory);

        if (savedCategory == null) {
            throw new CategorySaveFailedException("Failed to update category");
        }

        response.setResponseMessage("Category Updated Successfully");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<CategoryResponseDTO> fetchAllCategory() {

        LOG.info("Request received for fetching all categories");

        CategoryResponseDTO response = new CategoryResponseDTO();

        List<Category> categories = new ArrayList<>();

        categories = this.categoryService.getAllCategoriesDeletedFalse();

        if (CollectionUtils.isEmpty(categories)) {
            response.setResponseMessage("No Categories found");
            response.setSuccess(false);

            return new ResponseEntity<CategoryResponseDTO>(response, HttpStatus.OK);
        }

        response.setCategories(categories);
        response.setResponseMessage("Category fetched successful");
        response.setSuccess(true);

        return new ResponseEntity<CategoryResponseDTO>(response, HttpStatus.OK);
    }

    public ResponseEntity<CategoryResponseDTO> fetchAllCategoryDeleteTrue() {

        LOG.info("Request received for fetching all categories");

        CategoryResponseDTO response = new CategoryResponseDTO();

        List<Category> categories = new ArrayList<>();

        categories = this.categoryService.getAllCategoriesDeletedTrue();

        if (CollectionUtils.isEmpty(categories)) {
            response.setResponseMessage("No Categories found");
            response.setSuccess(false);

            return new ResponseEntity<CategoryResponseDTO>(response, HttpStatus.OK);
        }

        response.setCategories(categories);
        response.setResponseMessage("Category fetched successful");
        response.setSuccess(true);

        return new ResponseEntity<CategoryResponseDTO>(response, HttpStatus.OK);
    }

    public ResponseEntity<CommonApiResponse> reStoreCategory(int id) {
        LOG.info("Request received to restock category with ID: {}", id);

        CommonApiResponse response = new CommonApiResponse();

        if (id == 0) {
            response.setResponseMessage("Missing category ID");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            response.setResponseMessage("Category not found");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!category.isDeleted()) {
            response.setResponseMessage("Category is already active");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        category.setStatus(Constant.ActiveStatus.ACTIVE.value());
        category.setDeleted(false);
        category.setDeletedAt(null);
        category.setUpdateAt(LocalDateTime.now());

        Category updatedCategory = categoryService.updateCategory(category);

        if (updatedCategory == null) {
            throw new CategorySaveFailedException("Failed to restock the Category");
        }

        response.setResponseMessage("Category restocked successfully");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public ResponseEntity<CommonApiResponse> deleteCategoryPermanently(int categoryId) {
        LOG.info("Request received for permanently deleting category with ID: {}", categoryId);

        CommonApiResponse response = new CommonApiResponse();

        if (categoryId == 0) {
            response.setResponseMessage("Missing category ID");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra xem category có tồn tại không
        Optional<Category> optionalCategory = categoryDAO.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            response.setResponseMessage("Category not found");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            // Xóa category khỏi database
            categoryDAO.deleteById(categoryId);
            response.setResponseMessage("Category deleted permanently");
            response.setSuccess(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Error deleting category: {}", e.getMessage());
            response.setResponseMessage("Failed to delete category");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void fetchCategoryImage(String categoryImageName, HttpServletResponse resp) {
        Resource resource = storageService.loadCategoryImage(categoryImageName);
        if (resource == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(categoryImageName);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Mặc định nếu không xác định được loại file
        }

        resp.setContentType(mimeType);

        try (InputStream in = resource.getInputStream();
             ServletOutputStream out = resp.getOutputStream()) {
            FileCopyUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<CommonApiResponse> deleteCategory(int categoryId) {

        LOG.info("Request received for deleting category");

        CommonApiResponse response = new CommonApiResponse();

        if (categoryId == 0) {
            response.setResponseMessage("missing category Id");
            response.setSuccess(false);

            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = this.categoryService.getCategoryById(categoryId);

        if (category == null) {
            response.setResponseMessage("category not found");
            response.setSuccess(false);

            return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Product> products = this.productService.getByCategoryAndStatus(category, Constant.ActiveStatus.ACTIVE.value());
        category.setStatus(Constant.ActiveStatus.DELETED.value());
        category.setDeleted(true);
        category.setDeletedAt(LocalDateTime.now());
        Category updatedCategory = this.categoryService.updateCategory(category);

        if (updatedCategory == null) {
            throw new CategorySaveFailedException("Failed to delete the Category");
        }

        if (!CollectionUtils.isEmpty(products)) {

            for (Product product : products) {
                product.setStatus(Constant.ActiveStatus.DEACTIVATED.value());
            }

            List<Product> updatedCourses = this.productService.updateAll(products);

            if (CollectionUtils.isEmpty(updatedCourses)) {
                throw new CategorySaveFailedException("Failed to delete the Course Category!!!");
            }

        }

        response.setResponseMessage("Course Category & all its Courses Deleted Successful");
        response.setSuccess(true);

        return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

    }
}
