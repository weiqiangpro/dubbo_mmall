package com.wq.mmall.control;

import com.wq.mmall.annotation.ResponseAdvice;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.service.ICartService;
import com.wq.mmall.utils.Const;
import com.wq.mmall.vo.CartResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by weiqiang
 */

@Api(tags = "购物车相关的api")
@RestController
@RequestMapping("/cart/")
@ResponseAdvice
public class CartController {

    private final ICartService iCartService;

    @Autowired
    public CartController(ICartService iCartService) {
        this.iCartService = iCartService;
    }

    @GetMapping("list.do")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "分类的id，默认为0", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据的数量", dataType = "int"),
    })
    @ApiOperation("获取当前购物车商品")
    public CartResponse list(@RequestParam("userId") Integer userId,
                             @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                             HttpServletRequest request) throws MmallException {
        return iCartService.list(userId, pageNum, pageSize);
    }


    @ApiOperation("向当前购物车添加商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "productId", value = "商品Id", required = true, dataType = "int")
    })
    @PostMapping("add.do")
    public String add(@RequestParam("userId") Integer userId,
                      @RequestParam("productId") Integer productId,
                      HttpServletRequest request) throws MmallException {
        return iCartService.add(userId, productId);
    }

    @ApiOperation("删除当前购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "productId", value = "商品Id", required = true, dataType = "int")
    })
    @DeleteMapping("delete.do")
    public String deleteProduct(@RequestParam("userId") Integer userId,
                                @RequestParam("productId") Integer productId,
                                HttpServletRequest request) throws MmallException {
        return iCartService.deleteProduct(userId, productId);
    }

    @ApiOperation("获取当前购物车商品数量")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int")
    @GetMapping("product_count.do")
    public Integer getCartProductCount(@RequestParam("userId") Integer userId, HttpServletRequest request) {
        return iCartService.getCartProductCount(userId);
    }

    @ApiOperation("购物车中商品+1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "productId", value = "商品Id", required = true, dataType = "int")
    })
    @PutMapping("add_product.do")
    public String addProduct(@RequestParam("userId") Integer userId,
                             @RequestParam("productId") Integer productId,
                             HttpServletRequest httpServletRequest) throws MmallException {
        return iCartService.updateQuantity(Const.Cart.ADD, userId, productId);
    }

    @ApiOperation("购物车中商品-1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "productId", value = "商品Id", required = true, dataType = "int")
    })
    @PutMapping("reduce_product.do")
    public String reduceProduct(@RequestParam("userId") Integer userId,
                             @RequestParam("productId") Integer productId,
                             HttpServletRequest httpServletRequest) throws MmallException {
        return iCartService.updateQuantity(Const.Cart.REDUCE, userId, productId);
    }


    @ApiOperation("选中当前购物车中商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "productId", value = "商品Id", required = true, dataType = "int")
    })
    @PutMapping("select.do")
    public String select(@RequestParam("userId") Integer userId,
                         @RequestParam("productId") Integer productId,
                         HttpServletRequest httpServletRequest) throws MmallException {
        return iCartService.selectOrUnSelect(userId, productId, Const.Cart.CHECKED);
    }

    @ApiOperation("取消选中购物车中物品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(name = "productId", value = "商品Id", required = true, dataType = "int")
    })
    @PutMapping("un_select.do")
    public String unSelect(@RequestParam("userId") Integer userId,
                           @RequestParam("productId") Integer productId,
                           HttpServletRequest httpServletRequest) throws MmallException {

        return iCartService.selectOrUnSelect(userId, productId, Const.Cart.UN_CHECKED);
    }

}