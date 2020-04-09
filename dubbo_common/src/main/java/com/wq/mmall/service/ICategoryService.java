package com.wq.mmall.service;


import com.wq.mmall.entity.Category;
import com.wq.mmall.exception.MmallException;

import java.util.List;
import java.util.Optional;

/**
 * Created by weiqiang
 */

public interface ICategoryService {

    List<Category> getChildrenParallelCategory(Integer categoryId) throws MmallException;

   Category selectById(Integer categoryId) throws MmallException;

    List<Integer> selectCategoryAndChildrenById(Integer categoryId);

    String deleteCategory(Integer categoryId) throws MmallException;

    String updateCategory(Category category) throws MmallException;




}
