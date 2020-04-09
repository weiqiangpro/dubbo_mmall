package com.wq.mmall.service;


import com.wq.mmall.entity.Cart;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.vo.CartResponse;
import java.util.List;
/**
 * Created by weiqiang
 */

public interface ICartService {
    String add(Integer userId, Integer productId) throws MmallException;
    String deleteProduct(Integer userId, Integer productId) throws MmallException;
    CartResponse list(Integer userId, Integer pageNum, Integer pageSize) throws MmallException;
//    Cart selectOrUnSelect(Integer userId, Integer productId, Integer checked);
    String updateQuantity(Integer num , Integer userId,Integer productId) throws MmallException;
    Integer getCartProductCount(Integer userId);
    String selectOrUnSelect(Integer userId, Integer checked, Integer productId) throws MmallException;
    List<Cart>  selectCheckedCartByUserId(Integer userId);
}
