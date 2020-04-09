package com.wq.mmall.control;

import com.wq.mmall.annotation.ResponseAdvice;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.service.IOrderService;
import com.wq.mmall.vo.OrderProductVo;
import com.wq.mmall.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * Created by weiqiang
 */

@RestController
@Api(tags = "订单相关的api")
@RequestMapping("/order/")
@ResponseAdvice
public class OrderController {


    private final IOrderService iOrderService;

    @Autowired
    public OrderController(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
    }

    @ApiOperation(value = "通过购物车提交订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "shippingId", value = "收货地址id", required = true, dataType = "int"),
    })
    @PostMapping("commit.do")
    public OrderVo commit(@RequestParam("userId") int userId,
                          @RequestParam("shippingId") Integer shippingId,
                          HttpServletRequest request) throws MmallException {
        return iOrderService.createOrder(userId, shippingId);
    }

    @ApiOperation(value = "获得自己所有订单")
    @GetMapping("list.do")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "分类的id，默认为0", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据的数量", dataType = "int"),
    })
    public Page<OrderVo> list(@RequestParam("userId") int userId,
                              @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                              HttpServletRequest request) throws MmallException {
        return iOrderService.getOrderList(userId, pageNum, pageSize);
    }

    @ApiOperation(value = "获得订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "int")})
    @GetMapping("detail.do")
    public OrderVo detail(@RequestParam("userId") int userId,
                          @RequestParam("orderNo") long orderNo,
                          HttpServletRequest request) throws MmallException {
        return iOrderService.getOrderDetail(userId, orderNo);
    }

    @ApiOperation(value = "支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "int")})
    @PostMapping("pay.do")
    public String pay(@RequestParam("userId") int userId,
                      @RequestParam("orderNo") long orderNo,
                      HttpServletRequest request) throws MmallException {
        return iOrderService.pay(orderNo,userId);
    }


    @ApiOperation(value = "获得购物车商品列表")
    @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int")
    @GetMapping("get_order_cart_product.do")
    public OrderProductVo getOrderCartProduct(@RequestParam("userId") int userId, HttpServletRequest httpServletRequest) throws MmallException {
        return iOrderService.getOrderCartProduct(userId);
    }
}
