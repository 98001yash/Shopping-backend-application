package com.company.shoppingApplication.repository;

import com.company.shoppingApplication.entities.Category;
import com.company.shoppingApplication.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);

}
