package com.wq.mmall.control;

import com.wq.mmall.entity.Category;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by weiqiang
 */
@RestController
@RequestMapping("/category/")
@Api(tags = "商品类目相关api")
public class CategoryController {

    private final ICategoryService iCategoryService;

    @Autowired
    public CategoryController(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    //根据父分类获取分类
    @ApiOperation(value = "获取所有分类类目")
    @GetMapping("categories")
    public List<Category> getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) throws MmallException {
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

}
