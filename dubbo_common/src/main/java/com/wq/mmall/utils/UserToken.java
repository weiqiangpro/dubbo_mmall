package com.wq.mmall.utils;

import com.alibaba.fastjson.JSON;
import com.wq.mmall.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by weiqiang
 */
@Component
@Slf4j
public class UserToken {

    private final StringRedisTemplate redis;

    @Autowired
    public UserToken(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public  int getid(HttpServletRequest request) {
        String header = request.getHeader("Token");
        if (StringUtils.isBlank(header)) {
            return 0;
        }
        String token = redis.opsForValue().get(header);
        User user = JSON.parseObject(token, User.class);
        if (user == null) {
            return 0;
        }
        return user.getId();
    }

    public  User getUser(HttpServletRequest request) {
        String header = request.getHeader("Token");
        if (StringUtils.isBlank(header)) {
            return null;
        }
        String token = redis.opsForValue().get(header);
        return JSON.parseObject(token, User.class);
    }
}
