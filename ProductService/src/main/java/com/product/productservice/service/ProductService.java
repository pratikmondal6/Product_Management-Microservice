package com.product.productservice.service;

import com.product.productservice.entity.Product;
import com.product.productservice.model.ProductRequestModel;
import com.product.productservice.model.ProductResponseModel;
import com.product.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequestModel productRequest){

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is added", product.getId());

    }

    public List<ProductResponseModel> getAllProducts() {

       List<Product> products = productRepository.findAll();
       return products.stream().map(product -> mapToProductResponse(product)).toList();
    }

    private ProductResponseModel mapToProductResponse(Product product){
        return ProductResponseModel.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
