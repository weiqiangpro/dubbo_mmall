package com.wq.mmall.service;
import com.wq.mmall.entity.User;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.vo.LoginSuccessResponse;

/**
 * Created by weiqiang
 */

public interface IUserService {

    LoginSuccessResponse login(String username, String password) throws MmallException;

    String register(User user) throws MmallException;

    String update(User user) throws MmallException;

    User getUser(String token) throws MmallException;
}
