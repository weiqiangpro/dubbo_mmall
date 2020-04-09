package com.wq.mmall.dao;

import com.wq.mmall.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Author: weiqiang
 * Time: 2020/4/2 下午8:35
 */
public interface OrderDao extends JpaRepository<Order, Integer> {
    Page<Order> findByUserId(Integer userIdOrderDao,Pageable pageable);
    Optional<Order> findByUserIdAndOrderNo(Integer userId, Long orderNo);

}