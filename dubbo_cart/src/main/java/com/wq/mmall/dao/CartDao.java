package com.wq.mmall.dao;

import com.wq.mmall.entity.Cart;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Author: weiqiang
 * Time: 2020/4/2 下午8:35
 */
@SuppressWarnings("all")
public interface CartDao extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserIdAndProductId(Integer userId, Integer productId);

    Integer deleteByUserIdAndProductId(Integer userId, Integer productId);

    Page<Cart> findByUserId(Integer userId, Pageable pageable);
    List<Cart> findByUserId(Integer userId);

    List<Cart> findByCheckedAndUserId(Integer checked,Integer userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update mmall_cart c set c.checked = ?1 where c.user_id = ?2 and c.product_id = ?3",nativeQuery = true)
    Integer checkedOrUncheckedProduct(Integer checked,Integer userId, Integer productId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update mmall_cart c set c.quantity = c.quantity +?1 where c.user_id = ?2 and c.product_id = ?3",nativeQuery = true)
    Integer numByUserIdAndProductId(Integer num,Integer userId,Integer productId);
}