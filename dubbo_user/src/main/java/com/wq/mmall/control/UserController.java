package com.wq.mmall.control;


import com.wq.mmall.annotation.ResponseAdvice;
import com.wq.mmall.entity.User;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.service.IUserService;
import com.wq.mmall.service.UserServiceImpl;
import com.wq.mmall.vo.LoginSuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by weiqiang
 */
@RestController
@RequestMapping("/user/")
@Api(tags = "用户相关的api")
@ResponseAdvice
public class UserController {


    private final IUserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
    })
    @PostMapping("login")
    public LoginSuccessResponse login(String username, String password) throws MmallException {
        return userService.login(username, password);
    }


    @PostMapping("register")
    @ApiOperation("注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "passwd1", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "passwd2", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "question", value = "问题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "answaer", value = "问题", required = true, dataType = "String"),
    })
    public String register(@RequestParam("username") String username,
                           @RequestParam("passwd1") String passwd1,
                           @RequestParam("passwd2") String passwd2,
                           @RequestParam("email") String email,
                           @RequestParam("phone") String pho,
                           @RequestParam("question") String question,
                           @RequestParam("answaer") String answer) throws MmallException {
        if (!passwd1.equals(passwd2))
            throw new MmallException("两次密码输入不一致");
        User user = new User().setUsername(username).setPassword(passwd1).setEmail(email).setPhone(pho).setQuestion(question).setAnswer(answer);
        return userService.register(user);
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("info.do")
    public User getUserInfo(HttpServletRequest request) throws MmallException {

        User user = userService.getUser(request.getHeader("Token"));
        if (user == null)
            throw new MmallException("token不合法");
        return user;
    }

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }


}