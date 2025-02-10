/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 11:02 PM
 */
package com.example.springboottest.controller;

import com.example.springboottest.common.Page;
import com.example.springboottest.common.Result;
import com.example.springboottest.entity.User;
import com.example.springboottest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin    // Allow cross-domain access

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired  // Take user information
    UserService userService;

    // Add user information
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.insertUser(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("Insert failed!");
            } else {
                return Result.error("System error!");
            }
        }
        return Result.success();
    }

    // Update user information
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }

    // Delete multiple users
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        userService.batchDeleteUser(ids);
        return Result.success();
    }

    // Search all users
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.selectAll();
        return Result.success(userList);
    }

    // Single user search
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    // Multiple user search
    @GetMapping("/selectByName/{name}")
    public Result selectByName(@PathVariable String name) {
        List<User> userList = userService.selectByName(name);
        return Result.success(userList);
    }

    // Multiple user search
    @GetMapping("/selectByMul")
    public Result selectByMul(@RequestParam String username, @RequestParam String name) {
        List<User> user = userService.selectByMul(username, name);
        return Result.success(user);
    }

    // Fuzed search
    @GetMapping("/selectByFuzzy")
    public Result selectByFuzzy(@RequestParam String username, @RequestParam String name) {
        List<User> user = userService.selectByFuzzy(username, name);
        return Result.success(user);
    }

    // Page
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name) {
        Page<User> page = userService.selectByPage(pageNum, pageSize, username, name);
        return Result.success(page);
    }
}
