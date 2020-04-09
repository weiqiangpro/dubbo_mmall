package com.wq.mmall.dao;

import com.wq.mmall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author: weiqiang
 * Time: 2020/4/2 下午8:35
 */
public interface ProductDao extends JpaRepository<Product, Integer> {

    Page<Product> findByNameLikeAndCategoryIdIn(String productName, List<Integer> categoryIdList, Pageable pageable);

    Page<Product> findByNameLike(String key, Pageable pageable);

    Page<Product> findByCategoryIdIn(List<Integer> categoryIdList, Pageable pageable);

}