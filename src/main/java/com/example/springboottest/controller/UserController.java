/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 11:02 PM
 */
package com.example.springboottest.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboottest.GlobalException.ServiceException;
import com.example.springboottest.common.Result;
import com.example.springboottest.entity.User;
import com.example.springboottest.service.UserService;
import com.example.springboottest.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    // Export batch data
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String ids,
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<User> list = new ArrayList<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        if (StrUtil.isNotBlank(ids)) {
            // Second option, export selected data
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id", idsArr);
        } else {
            // First option, export all data
            // QueryWrapper
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        }
        list = userService.list(queryWrapper);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("usersDatasheet", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

    /*
    Batch import
    @param file send excel file object
    @return Result
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        // Write data to database
        try {
            userService.saveBatch(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Batch import failed!");
        }
        return Result.success();
    }
}
