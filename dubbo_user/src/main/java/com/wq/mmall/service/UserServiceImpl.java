package com.wq.mmall.service;

import com.alibaba.fastjson.JSON;
import com.wq.mmall.dao.UserDao;
import com.wq.mmall.entity.User;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.utils.JwtToken;
import com.wq.mmall.utils.MD5Util;
import com.wq.mmall.vo.LoginSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by weiqiang
 */

@Service
@Transactional(rollbackFor = {RpcException.class,MmallException.class})
@Slf4j
public class UserServiceImpl implements IUserService {

    private  UserDao userDao;
    private  StringRedisTemplate redis;

    @Autowired
    public UserServiceImpl(UserDao userDao, StringRedisTemplate redis) {
        this.userDao = userDao;
//        this.redis = redis;
    }

    @Override
    public LoginSuccessResponse login(String username, String password) throws MmallException {

        User user = userDao.findByUsername(username);
        if (null == user) {
           throw new MmallException("用户不存在");
        }

        if (StringUtils.isEmpty(password) || !MD5Util.MD5EncodeUtf8(password).equals(user.getPassword())) {
            throw new MmallException("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        String json = JSON.toJSONString(user);
        String token = null;

        try {
            token = JwtToken.createToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        redis.opsForValue().set(token, json, 30, TimeUnit.MINUTES);
        return  new LoginSuccessResponse("登陆成功", token);
    }

    @Override
    public String register(User user) throws MmallException {
        if (null != userDao.findByUsername(user.getUsername()))
           throw new MmallException("用户名已存在");
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        User result = userDao.save(user);
        if (null == result) {
            throw new MmallException("注册失败");
        }
        return "注册成功";
    }

    @Override
    public String update(User user) throws MmallException {
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        User result = userDao.saveAndFlush(user);
        if (null != result) {
            throw new MmallException("修改失败");
        }
        return "修改失败";
    }

    @Override
    public User getUser(String token) throws MmallException {
        String s = redis.opsForValue().get(token);
        if (s == null) {
            throw new MmallException("获取用户失败");
        }
        User user = JSON.parseObject(s, User.class);
        user.setAnswer("");
        return user;
    }
}