package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dto.ProductDTO;
import com.example.Jewelry.dto.response.ImageDTO;
import com.example.Jewelry.entity.Category;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.service.ProductService;
import com.example.Jewelry.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ReviewService reviewService;


    @Override
    public Product add(Product product) {
        // TODO Auto-generated method stub
        return productDAO.save(product);
    }

    @Override
    public Product update(Product course) {
        // TODO Auto-generated method stub
        return productDAO.save(course);
    }

    @Override
    public Product getById(int id) {

        Optional<Product> optional = this.productDAO.findById(id);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }

    }

    @Override
    public List<Product> getAll() {
        // TODO Auto-generated method stub
        return productDAO.findAll();
    }

    @Override
    public List<Product> getByStatus(String status) {
        // TODO Auto-generated method stub
        return productDAO.findByStatusOrderByIdDesc(status);
    }

    @Override
    public List<Product> getByCategoryAndStatus(Category category, String status) {
        // TODO Auto-generated method stub
        return productDAO.findByCategoryAndStatusOrderByIdDesc(category, status);
    }

    @Override
    public List<Product> getByNameAndStatus(String name, String status) {
        // TODO Auto-generated method stub
        return productDAO.findByStatusAndNameContainingIgnoreCaseOrderByIdDesc(status, name);
    }

    @Override
    public List<Product> fetchAllProductDeleteTrue() {
        return this.productDAO.findByDeletedTrue();
    }

    @Override
    public List<Product> fetchAllProductDeleteFalse() {
        return this.productDAO.findByDeletedFalse();
    }

    @Override
    public void deleteProduct(int productId) {
        this.productDAO.deleteById(productId);
    }

    @Override
    public List<Product> updateAll(List<Product> courses) {
        // TODO Auto-generated method stub
        return productDAO.saveAll(courses);
    }
    @Override
    public List<ProductDTO> getActiveProductListForShop() {
        List<Product> products = productDAO.findByDeletedFalse();
        return products.stream()
                .filter(p -> "Active".equalsIgnoreCase(p.getStatus()))
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .prevPrice(product.getPrevPrice())
                        .description(product.getDescription())
                        .brand(product.getBrand())
                        .productMaterial(product.getProductMaterial())
                        .occasion(product.getOccasion())
                        .productIsBadge(product.getProductIsBadge())
                        .status(product.getStatus())
                        .imageURLs(product.getImages().stream()
                                .map(img -> new ImageDTO(img.getId(), img.getUrl()))
                                .collect(Collectors.toList()))
                        .averageRating(reviewService.getAverageRatingForProduct(product.getId()))
                        .totalRating(reviewService.getTotalReviewsForProduct(product.getId()))
                        .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brand(product.getBrand())
                .productMaterial(product.getProductMaterial())
                .occasion(product.getOccasion())
                .productIsBadge(product.getProductIsBadge())
                        .build())
                .toList();

    }

    @Override
    public ProductDTO getProductDetailsForUser(int id) {
        Product product = getById(id);
        if (product == null || product.isDeleted() || !"active".equalsIgnoreCase(product.getStatus())) {
            return null;
        }
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .productMaterial(product.getProductMaterial())
                .occasion(product.getOccasion())
                .prevPrice(product.getPrevPrice())
                .price(product.getPrice())
                .productIsBadge(product.getProductIsBadge())
                .imageURLs(product.getImages().stream()
                        .map(img -> new ImageDTO(img.getId(), img.getUrl()))
                        .collect(Collectors.toList()))
                .averageRating(2) // sẽ thay sau nếu có
                .totalRating(2)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brand(product.getBrand())
                .productMaterial(product.getProductMaterial())
                .description(product.getDescription())
                .price(product.getPrice())
                .size(product.getSize())
                .occasion(product.getOccasion())
                .prevPrice(product.getPrevPrice())
                .productIsBadge(product.getProductIsBadge())
                .status(product.getStatus())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .averageRating(reviewService.getAverageRatingForProduct(product.getId()))
                .totalRating(reviewService.getTotalReviewsForProduct(product.getId()))
                .build();
    }

    @Override
    public List<Product> getByCategoryNameAndStatus(String categoryName, String status) {
        return productDAO.getByCategoryNameAndStatus(categoryName, status);
    }

    @Override
    public List<Product> fetchAllProductOpenAuction(String status) {
        return productDAO.findAllByStatusOpenAuction(status);
    }



}
