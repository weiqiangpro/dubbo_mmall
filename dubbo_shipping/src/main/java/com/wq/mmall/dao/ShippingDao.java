package com.wq.mmall.dao;

import com.wq.mmall.entity.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <h1>CouponTemplate Dao 接口定义</h1>
 * Created by Qinyi.
 */

public interface ShippingDao extends JpaRepository<Shipping, Integer> {

    Integer deleteByUserIdAndId(Integer userId, Integer id);

    Integer deleteByIdAndUserId(Integer id, Integer userId);

    Optional<Shipping> findByIdAndUserId(Integer id, Integer userId);

    Page<Shipping> findByUserId(Integer userId, Pageable pageable);

}