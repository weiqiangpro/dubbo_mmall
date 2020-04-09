package com.wq.mmall.dao;

import com.wq.mmall.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author: weiqiang
 * Time: 2020/4/2 下午8:35
 */
public interface CategoryDao extends JpaRepository<Category, Integer> {

    List<Category> findByParentId(Integer parentId);
}