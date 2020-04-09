package com.wq.mmall.control;

import com.wq.mmall.annotation.ResponseAdvice;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.service.IProductService;
import com.wq.mmall.vo.ProductDetailResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Created by weiqiang
 */

@RestController
@RequestMapping("/product/")
@Api(tags = "商品相关的api")
@ResponseAdvice
public class ProductController {

    private final IProductService iProductService;

    @Autowired
    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @ApiOperation(value = "Restful形式获取商品详细信息")
    @ApiImplicitParam(name = "productId", value = "商品id", required = true, paramType = "path", dataType = "int")
    @GetMapping("/{productId}")
    public ProductDetailResponse detailRESTful(@PathVariable Integer productId) throws MmallException {
        return iProductService.getProductDetail(productId);
    }

    @ApiOperation(value = "根据条件获取内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String"),
            @ApiImplicitParam(name = "categoryId", value = "分类的id", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "分类的id，默认为0", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据的数量", dataType = "int"),
            @ApiImplicitParam(name = "orderBy", value = "排序,price_desc或者price_asc", dataType = "String")
    })
    @PostMapping("list")
    public Page list(@RequestParam(value = "keyword", required = false) String keyword,
                     @RequestParam(value = "categoryId", required = false) Integer categoryId,
                     @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                     @RequestParam(value = "orderBy", defaultValue = "") String orderBy) throws MmallException {
        return iProductService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}
