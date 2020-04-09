package com.wq.mmall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wq.mmall.dao.OrderDao;
import com.wq.mmall.dao.OrderItemDao;
import com.wq.mmall.entity.*;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.utils.BigDecimalUtil;
import com.wq.mmall.utils.Const;
import com.wq.mmall.utils.DateTimeUtil;
import com.wq.mmall.vo.OrderItemVo;
import com.wq.mmall.vo.OrderProductVo;
import com.wq.mmall.vo.OrderVo;
import com.wq.mmall.vo.ShippingVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by weiqiang
 */

@Service
@Slf4j
@Transactional(rollbackFor = {RpcException.class,MmallException.class})
public class OrderServiceImpl implements IOrderService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    @Reference
    private IShippingService shippingService;
    @Reference
    private IProductService productService;
    @Reference
    private ICartService cartService;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public OrderVo createOrder(Integer userId, Integer shippingId) throws MmallException {

        //从购物车中获取数据
        List<Cart> cartList = cartService.selectCheckedCartByUserId(userId);
        //计算这个订单的总价
        List<OrderItem> orderItemList = this.getCartOrderItem(userId, cartList);
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);

        //生成订单
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (order == null) {
            throw new MmallException("生成订单错误");
        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new MmallException("购物车为空");
        }
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        //mybatis 批量插入
        orderItemDao.saveAll(orderItemList);
        //生成成功,我们要减少我们产品的库存
        this.reduceProductStock(orderItemList);
        //清空一下购物车
        this.cleanCart(cartList);
        return assembleOrderVo(order, orderItemList);
    }


    @Override
    public OrderProductVo getOrderCartProduct(Integer userId) throws MmallException {
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车中获取数据

        List<Cart> cartList = cartService.selectCheckedCartByUserId(userId);
        List<OrderItem> orderItemList = this.getCartOrderItem(userId, cartList);
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
//        orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return orderProductVo;
    }


    @Override
    public Page<OrderVo> getOrderList(Integer userId, int pageNum, int pageSize) throws MmallException {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Order> orders = orderDao.findByUserId(userId, pageable);
        List<OrderVo> orderVoList = assembleOrderVoList(orders.getContent(), userId);
        return new PageImpl<>(orderVoList, pageable, orders.getTotalElements());
    }

    @Override
    public OrderVo getOrderDetail(Integer userId, Long orderNo) throws MmallException {
        Optional<Order> order = orderDao.findByUserIdAndOrderNo(userId, orderNo);
        if (order.isPresent()) {
            List<OrderItem> orderItemList = orderItemDao.findByUserIdAndOrderNo(userId, orderNo);
            return assembleOrderVo(order.get(), orderItemList);
        }
        throw new MmallException("没有找到该订单");
    }


    public String pay(Long orderNo,Integer userId) throws MmallException {
        Map<String ,String> resultMap = Maps.newHashMap();
        Optional<Order> order = orderDao.findByUserIdAndOrderNo(userId,orderNo);
        if(!order.isPresent()){
            throw new MmallException("用户没有该订单");
        }
        Order newOrder = order.get();
        newOrder.setPaymentTime(new Date());
        orderDao.saveAndFlush(newOrder);
        return "支付成功";
    }


    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) throws MmallException {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingService.select(order.getUserId(), order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }

        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));


        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }


    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }


    private ShippingVo assembleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

    private void cleanCart(List<Cart> cartList) throws MmallException {
        for (Cart cart : cartList) {
            cartService.deleteProduct(cart.getUserId(), cart.getProductId());
        }
    }


    private List<OrderItem> getCartOrderItem(Integer userId, List<Cart> cartList) throws MmallException {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {
            throw new MmallException("购物车为空");
        }
        //校验购物车的数据,包括产品的状态和数量
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productService.getProductById(cartItem.getProductId());
            if (Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                throw new MmallException("产品" + product.getName() + "不是在线售卖状态");
            }
            //校验库存
            if (cartItem.getQuantity() > product.getStock()) {
                throw new MmallException("产品" + product.getName() + "库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity()));
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) throws MmallException {
        Order order = new Order();
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        Order order1 = orderDao.saveAndFlush(order);

        if (order1.getId() != 0) {
            return order;
        }
        throw new MmallException("订单生成失败");
    }


    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private void reduceProductStock(List<OrderItem> orderItemList) throws MmallException {
        for (OrderItem orderItem : orderItemList) {

            Product product = productService.getProductById(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productService.update(product);
        }
    }


    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) throws MmallException {
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemDao.findByUserIdAndOrderNo(userId, order.getOrderNo());
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

}
