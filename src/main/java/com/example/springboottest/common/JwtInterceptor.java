/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/11/2025 8:44 PM
 */
package com.example.springboottest.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springboottest.GlobalException.ServiceException;
import com.example.springboottest.entity.User;
import com.example.springboottest.mapper.UserMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Seven-step approach
        String token = request.getHeader("token");  // Attribute from the header
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token"); // url parameter ?token=xxxx
        }
        // if not using the method then exclude from intercept
        if (handler instanceof HandlerMethod) {
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null) {
                return true;
            }
        }

        // Checking the token
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401", "Please login first!");
        }
        // Take the user id from token
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);    // Decode jwt token
        }catch (JWTDecodeException j){
            throw new ServiceException("401", "Please login first!");
        }

        // Search the user id in the database
        User user = userMapper.selectById(Integer.valueOf(userId));
        if(user == null){
            throw new ServiceException("401", "Please login first!");
        }

        // Use the user's password to generate a verifier (remember to use corresponding attribute)
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch(JWTVerificationException e){
            throw new ServiceException("401", "Please login first!");
        }
        return true;
    }
}
