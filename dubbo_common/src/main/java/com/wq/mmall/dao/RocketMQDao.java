package com.wq.mmall.dao;

import com.wq.mmall.entity.Category;
import com.wq.mmall.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Author: weiqiang
 * Time: 2020/4/10 下午2:49
 */
public interface RocketMQDao extends JpaRepository<Transaction, Integer> {

    Optional<Transaction> findByTransaction(String id);
}
