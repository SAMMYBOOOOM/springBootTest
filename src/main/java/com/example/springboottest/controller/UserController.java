/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 11:02 PM
 */
package com.example.springboottest.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboottest.GlobalException.ServiceException;
import com.example.springboottest.common.Result;
import com.example.springboottest.entity.User;
import com.example.springboottest.service.UserService;
import com.example.springboottest.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin    // Allow cross-domain access

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource  // Take user information
    UserService userService;

    // Add user information
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.save(user);
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
        userService.updateById(user);
        return Result.success();
    }

    // Delete multiple users
    @DeleteMapping("/delete/{id}")
    public Result batchDelete(@PathVariable Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        if (id.equals(currentUser.getId())) {
            throw new ServiceException("Cannot delete current user!");
        }
        userService.removeById(id);
        return Result.success();
    }

    // Delete multiple users
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null && ids.contains(currentUser.getId())) {
            throw new ServiceException("Deletion contains current user!");
        }
        userService.removeBatchByIds(ids);
        return Result.success();
    }

    // Search all users
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.list(new QueryWrapper<User>().orderByDesc("id")); // SELECT * FROM user ORDER BY id DESC
        return Result.success(userList);
    }

    // Single user search
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    // Page
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");
        // Apply the like clause for username only if username is not blank
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        // Apply the like clause for name only if name is not blank
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        // Execute the query with pagination
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }
}
