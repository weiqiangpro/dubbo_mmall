package com.wq.mmall.service;

import com.wq.mmall.dao.CartDao;
import com.wq.mmall.entity.Cart;
import com.wq.mmall.entity.Product;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.utils.BigDecimalUtil;
import com.wq.mmall.utils.Const;
import com.wq.mmall.vo.CartProductResponse;
import com.wq.mmall.vo.CartResponse;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by weiqiang
 */
@Service
@Slf4j
//@Transactional(rollbackFor = {RpcException.class,MmallException.class})
public class CartServiceImpl implements ICartService {


    @Autowired
    private CartDao cartDao;

    @Reference(loadbalance = "random", timeout = 1000) //dubbo直连
    private IProductService productService;

    public String add(Integer userId, Integer productId) throws MmallException {
        Product product = productService.getProductById(productId);
        if (product == null)
            throw new MmallException("该产品不存在");
        Optional<Cart> cart = cartDao.findByUserIdAndProductId(userId, productId);
        if (cart.isPresent()) {
            return "该产品已放入购物车";
        }
        Cart c = new Cart().setChecked(0).setProductId(productId).setQuantity(1).setUserId(userId);
        cartDao.saveAndFlush(c);
        return "添加成功";

    }
    @Transactional(rollbackFor = {RpcException.class,MmallException.class})
    public String deleteProduct(Integer userId, Integer productId) throws MmallException {
        if (cartDao.deleteByUserIdAndProductId(userId, productId) <= 0)
            throw new MmallException("删除物品失败");
        return "删除物品成功";
    }


    public CartResponse list(Integer userId, Integer pageNum, Integer pageSize) throws MmallException {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return this.getCartVoLimit(userId, pageable);
    }

    @Override
    public String selectOrUnSelect(Integer userId, Integer productId, Integer checked) throws MmallException {
        Integer integer = cartDao.checkedOrUncheckedProduct(checked, userId, productId);
        if (integer < 1)
            throw new MmallException("取消或选中失败");
        return checked == 1 ? "选中成功" : "取消选中成功";
    }

    @Override
    public List<Cart> selectCheckedCartByUserId(Integer userId) {
        return cartDao.findByCheckedAndUserId(1,userId);
    }

    @Override
    @Transactional(rollbackFor = MmallException.class)
    public String updateQuantity(Integer num, Integer userId, Integer productId) throws MmallException {
        Optional<Cart> cart = cartDao.findByUserIdAndProductId(userId, productId);
        if (!cart.isPresent()){
            throw new MmallException("该商品不存在");
        }
        if (cart.get().getQuantity() <=0 && num<0)
            throw new MmallException("数量为0,不能减少");
        Integer ok = cartDao.numByUserIdAndProductId(num, userId, productId);
        if (ok > 0)
            return num > 0 ? "数量增加成功" : "数量减少成功";
        throw new MmallException("修改失败,未知错误");
    }

    @Override
    public Integer getCartProductCount(Integer userId) {

        List<Cart> carts = cartDao.findByUserId(userId);
        if (CollectionUtils.isEmpty(carts))
            return 0;
        return carts.size();
    }


    private CartResponse getCartVoLimit(Integer userId, Pageable pageable) throws MmallException {
        CartResponse cartResponse = new CartResponse();
        Page<Cart> cartPage = cartDao.findByUserId(userId, pageable);
        List<Cart> cartList = cartPage.getContent();
        List<CartProductResponse> cartProductResponseList = new ArrayList<>();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductResponse cartProductResponse = new CartProductResponse();
                cartProductResponse.setId(cartItem.getId());
                cartProductResponse.setUserId(userId);
                cartProductResponse.setProductId(cartItem.getProductId());

                Product product = productService.getProductById(cartItem.getProductId());
                if (product != null) {
                    cartProductResponse.setProductMainImage(product.getMainImage());
                    cartProductResponse.setProductName(product.getName());
                    cartProductResponse.setProductSubtitle(product.getSubtitle());
                    cartProductResponse.setProductStatus(product.getStatus());
                    cartProductResponse.setProductPrice(product.getPrice());
                    cartProductResponse.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        //库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        cartProductResponse.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductResponse.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartDao.saveAndFlush(cartForQuantity);
                    }
                    cartProductResponse.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductResponse.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductResponse.getQuantity()));
                    cartProductResponse.setProductChecked(cartItem.getChecked());
                }

                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductResponse.getProductTotalPrice().doubleValue());
                }
                cartProductResponseList.add(cartProductResponse);
            }
        }
        cartResponse.setCartTotalPrice(cartTotalPrice);
        cartResponse.setCartProductResponsePage(new PageImpl<>(cartProductResponseList, pageable, cartPage.getTotalElements()));
        return cartResponse;
    }
}
