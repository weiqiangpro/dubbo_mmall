package com.wq.mmall.service;


import com.wq.mmall.exception.MmallException;
import com.wq.mmall.vo.OrderProductVo;
import com.wq.mmall.vo.OrderVo;
import org.springframework.data.domain.Page;

/**
 * Created by weiqiang
 */

public interface IOrderService {
    OrderVo getOrderDetail(Integer userId,Long orderNo) throws MmallException;
    Page<OrderVo> getOrderList(Integer userId, int pageNum, int pageSize) throws MmallException;
    OrderProductVo getOrderCartProduct(Integer userId) throws MmallException;
    OrderVo createOrder(Integer userId, Integer shippingId) throws MmallException;
    String pay(Long orderNo,Integer userId) throws MmallException;

    //    ServerResponse<Page> manageList(int pageNum, int pageSize);
//
//    ServerResponse manageDetail(int ordeId);
//
//    ServerResponse<ProductDetailResponse> getByTime(String d1, String d2);
//
//    ServerResponse getByTimeNums(String d1, String d2);
//
//    ServerResponse categoryNums(String d1, String d2);
//
//    ServerResponse<String> manageUpdateGoods(int orderId, int status);
//
//    ServerResponse datesNums(String d1, String d2);

}
