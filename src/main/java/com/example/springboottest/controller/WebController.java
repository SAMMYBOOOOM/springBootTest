/**
 * Functionality:   Data retrieve
 * Author: Sam Hsu
 * Date: 2/4/2025 12:18 AM
 */
package com.example.springboottest.controller;

import cn.hutool.core.util.StrUtil;
import com.example.springboottest.common.AuthAccess;
import com.example.springboottest.common.Result;
import com.example.springboottest.entity.User;
import com.example.springboottest.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/web")
public class WebController {

    @Resource
    UserService userService;

    @AuthAccess
    @GetMapping("/")  // /web/hello
    public Result hello(){
        return Result.success("success");
    }

    // Exclude login from interceptor at InterceptorConfig.java
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("Username or password cannot be empty!");
        }
        user = userService.login(user);
        return Result.success(user);
    }

    @AuthAccess // Exclude register from interceptor
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getRole())) {
            return Result.error("Username or password cannot be empty!");
        }
        if(user.getUsername().length() > 10 || user.getPassword().length() > 20) {
            return Result.error("Username or password too long!");
        }
        user = userService.register(user);
        return Result.success(user);
    }

    // Reset password
    @AuthAccess
    @PutMapping("/password")
    public Result password(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPhone())) {
            return Result.error("Username or phone cannot be empty!");
        }
        userService.resetPassword(user);
        return Result.success();
    }

    // Verify user token
    @GetMapping("/auth/verify")
    public Result verifyToken() {
        // The JwtInterceptor will automatically verify the token
        // If we reach this point, the token is valid
        return Result.success();
    }
}
