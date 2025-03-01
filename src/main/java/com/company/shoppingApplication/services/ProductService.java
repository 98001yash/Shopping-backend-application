package com.company.shoppingApplication.services;

import com.company.shoppingApplication.dto.ProductDto;
import com.company.shoppingApplication.entities.Product;
import com.company.shoppingApplication.request.AddProductRequest;
import com.company.shoppingApplication.request.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    Product addProduct(AddProductRequest productdto);
    Product getProductById(Long id);

    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest productDto, Long productId);

    List<Product> getAllProducts();
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);

    List<Product> getProductByCategoryAndBrand(String category, String brand);

    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String category, String name);

    Long countProductByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
