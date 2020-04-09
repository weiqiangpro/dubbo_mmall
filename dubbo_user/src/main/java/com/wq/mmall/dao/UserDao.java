package com.wq.mmall.dao;

import com.wq.mmall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <h1>CouponTemplate Dao 接口定义</h1>
 * Created by Qinyi.
 */

public interface UserDao extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username,String password);

}
