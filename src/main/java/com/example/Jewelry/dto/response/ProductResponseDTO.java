package com.example.Jewelry.dto.response;

import com.example.Jewelry.dto.ProductDTO;
import com.example.Jewelry.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO extends CommonApiResponse {

    private List<Product> products = new ArrayList<>();

    private Product product;

    private String isProductPurchased;

    private List<ProductDTO> productDTOs;

    private ProductDTO productDTO;

    private Page<ProductDTO> productPage;

}
