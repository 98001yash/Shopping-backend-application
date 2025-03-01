package com.company.shoppingApplication.services.Impl;

import com.company.shoppingApplication.Exceptions.ProductNotFoundException;
import com.company.shoppingApplication.dto.ImageDto;
import com.company.shoppingApplication.dto.ProductDto;
import com.company.shoppingApplication.entities.Category;
import com.company.shoppingApplication.entities.Image;
import com.company.shoppingApplication.entities.Product;
import com.company.shoppingApplication.repository.CategoryRepository;
import com.company.shoppingApplication.repository.ImageRepository;
import com.company.shoppingApplication.repository.ProductRepository;
import com.company.shoppingApplication.request.AddProductRequest;
import com.company.shoppingApplication.request.ProductUpdateRequest;
import com.company.shoppingApplication.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    @Override
    public Product addProduct(AddProductRequest request) {
        if (request.getCategory() == null || request.getCategory().getName() == null) {
            throw new IllegalArgumentException("Category or Category Name cannot be null");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }


    private Product createProduct(AddProductRequest request, Category category){
      return new Product(
              request.getName(),
              request.getBrand(),
              request.getPrice(),
              request.getInventory(),
              request.getDescription(),
               category
      );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()->  new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
    productRepository.findById(id).ifPresentOrElse(productRepository::delete,()->{
        throw new ProductNotFoundException("Product Not found..");
    });

    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {

        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest productDto){
        existingProduct.setName(productDto.getName());
        existingProduct.setBrand(productDto.getBrand());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setInventory(productDto.getInventory());
        existingProduct.setDescription(productDto.getDescription());

        Category category = categoryRepository.findByName(productDto.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }
    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image-> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
