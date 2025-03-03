/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 11:02 PM
 */
package com.example.springboottest.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboottest.common.Result;
import com.example.springboottest.entity.Announcement;
import com.example.springboottest.entity.User;
import com.example.springboottest.service.AnnouncementService;
import com.example.springboottest.service.UserService;
import com.example.springboottest.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin    // Don't need this with CorsConfig.java

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;

    @Autowired
    UserService userService;

    // Add announcement information
    @PostMapping("/add")
    public Result add(@RequestBody Announcement announcement) {
        User currentUser = TokenUtils.getCurrentUser(); // Retrieve current user information
        announcement.setUserid(currentUser.getId());
        announcement.setDate(DateUtil.now());   // yyyy-mm-dd hh:mm:ss
        announcementService.save(announcement);
        return Result.success();
    }

    // Update announcement information
    @PutMapping("/update")
    public Result update(@RequestBody Announcement announcement) {
        announcementService.updateById(announcement);
        return Result.success();
    }

    // Delete single announcement
    @DeleteMapping("/delete/{id}")
    public Result batchDelete(@PathVariable Integer id) {
        announcementService.removeById(id);
        return Result.success();
    }

    // Delete multiple announcement
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        announcementService.removeBatchByIds(ids);
        return Result.success();
    }

    // Search all announcement
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Announcement> userList = announcementService.list(new QueryWrapper<Announcement>().orderByDesc("id")); // SELECT * FROM announcement ORDER BY id DESC
        return Result.success(userList);
    }

    // Search user announcement
    @GetMapping("/selectUserAnnouncement")
    public Result selectUserAnnouncement() {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<Announcement>().orderByDesc("id");
        queryWrapper.eq("publish", 1);  // User can only see announcements that have been published
        List<Announcement> userList = announcementService.list(queryWrapper); // SELECT * FROM announcement WHERE publish = 1 ORDER BY id DESC
        return Result.success(userList);
    }

    // Single announcement search
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Announcement announcement = announcementService.getById(id);
        User user = userService.getById(announcement.getUserid());
        if(user != null){
            announcement.setUser(user.getName());
        }
        return Result.success(announcement);
    }

    // Page
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String title) {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<Announcement>().orderByDesc("id");
        queryWrapper.like(StrUtil.isNotBlank(title), "title", title);
        // Execute the query with pagination
        Page<Announcement> page = announcementService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Announcement> records = page.getRecords();
//        List<User> list = userService.list();
        for(Announcement record : records) {
            Integer authorid = record.getUserid();
            User user = userService.getById(authorid);
//            String author = list.stream().filter(user -> user.getId().equals(authorid)).map(User::getName).findFirst().orElse("Unknown");
            if(user != null){
                record.setUser(user.getName());
            }
        }

        return Result.success(page);
    }
}
