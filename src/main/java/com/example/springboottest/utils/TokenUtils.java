/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/11/2025 9:28 PM
 */
package com.example.springboottest.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboottest.entity.User;
import com.example.springboottest.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

@Component  // Add it to spring container for line 30 to get the object
public class TokenUtils {

    private static UserMapper staticUserMapper;

    @Resource
    UserMapper userMapper;

    @PostConstruct
    public void setUserService() {
        staticUserMapper = userMapper;
    }

    /*
      Generate token
      @return
     */
    public static String createToken(String userId, String sign) {
        return JWT.create().withAudience(userId)    // Load user id into token as payload
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // Expiration time
                .sign(Algorithm.HMAC256(sign)); // Use password as token secret
    }

    /*
        Getting current user id
        @return user object
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserMapper.selectById(Integer.valueOf(userId));
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
