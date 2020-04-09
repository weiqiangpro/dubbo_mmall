package com.wq.mmall.dao;

import com.wq.mmall.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author: weiqiang
 * Time: 2020/4/2 下午8:35
 */
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByUserIdAndOrderNo(Integer userId, Long orderNo);
}