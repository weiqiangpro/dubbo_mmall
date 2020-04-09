package com.wq.mmall.advice;

import com.wq.mmall.annotation.ResponseAdvice;
import com.wq.mmall.vo.response.ServerResponse;
import org.apache.catalina.Server;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <h1>统一响应</h1>
 * Created by Qinyi.
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * <h2>判断是否需要对响应进行处理</h2>
     * */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {

        // 如果当前方法所在的类标识了 @IgnoreResponseAdvice 注解, 不需要处理
        if (methodParameter.getDeclaringClass().isAnnotationPresent(ResponseAdvice.class)) {
            return true;
        }

        // 如果当前方法标识了 @IgnoreResponseAdvice 注解, 不需要处理
        if (methodParameter.getMethod().isAnnotationPresent(ResponseAdvice.class)) {
            return true;
        }

        // 对响应进行处理, 执行 beforeBodyWrite 方法
        return false;
    }

    /**
     * <h2>响应返回之前的处理</h2>
     * */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        // 定义最终的返回对象
        ServerResponse response =  ServerResponse.createBySuccess();

        // 如果 o 是 null, response 不需要设置 data
        if (null == o) {
            return response;
            // 如果 o 已经是 CommonResponse, 不需要再次处理
        } else if (o instanceof ServerResponse) {
            response = (ServerResponse<Object>) o;
            // 否则, 把响应对象作为 CommonResponse 的 data 部分
        } else {
            response = ServerResponse.createBySuccess(o);
        }
        return response;
    }
}
