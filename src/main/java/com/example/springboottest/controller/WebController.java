/**
 * Functionality:   Data retrieve
 * Author: Sam Hsu
 * Date: 2/4/2025 12:18 AM
 */
package com.example.springboottest.controller;

import cn.hutool.core.util.StrUtil;
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

    @RequestMapping(value = "/", method = RequestMethod.POST)  // /web/hello
    public Result hello(){
        return Result.success("success");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("Username or password cannot be empty!");
        }
        user = userService.login(user);
        return Result.success(user);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        if(StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("Username or password cannot be empty!");
        }
        if(user.getUsername().length() > 10 || user.getPassword().length() > 20) {
            return Result.error("Username or password too long!");
        }
        user = userService.register(user);
        return Result.success(user);
    }
}
