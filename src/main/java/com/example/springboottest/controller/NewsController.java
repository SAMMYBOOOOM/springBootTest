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
import com.example.springboottest.entity.News;
import com.example.springboottest.entity.User;
import com.example.springboottest.service.NewsService;
import com.example.springboottest.service.UserService;
import com.example.springboottest.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin    // Don't need this with CorsConfig.java

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    // Add news information
    @PostMapping("/add")
    public Result add(@RequestBody News news) {
        User currentUser = TokenUtils.getCurrentUser(); // Retrieve current user information
        news.setAuthorid(currentUser.getId());
        news.setDate(DateUtil.now());   // yyyy-mm-dd hh:mm:ss
        newsService.save(news);
        return Result.success();
    }

    // Update news information
    @PutMapping("/update")
    public Result update(@RequestBody News news) {
        newsService.updateById(news);
        return Result.success();
    }

    // Delete single news
    @DeleteMapping("/delete/{id}")
    public Result batchDelete(@PathVariable Integer id) {
        newsService.removeById(id);
        return Result.success();
    }

    // Delete multiple news
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        newsService.removeBatchByIds(ids);
        return Result.success();
    }

    // Search all news
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<News> userList = newsService.list(new QueryWrapper<News>().orderByDesc("id")); // SELECT * FROM news ORDER BY id DESC
        return Result.success(userList);
    }

    // Single news search
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        News news = newsService.getById(id);
        User user = userService.getById(news.getAuthorid());
        if(user != null){
            news.setAuthor(user.getName());
        }
        return Result.success(news);
    }

    // Page
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String title) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<News>().orderByDesc("id");
        queryWrapper.like(StrUtil.isNotBlank(title), "username", title);
        // Execute the query with pagination
        Page<News> page = newsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<News> records = page.getRecords();
//        List<User> list = userService.list();
        for(News record : records) {
            Integer authorid = record.getAuthorid();
            User user = userService.getById(authorid);
//            String author = list.stream().filter(user -> user.getId().equals(authorid)).map(User::getName).findFirst().orElse("Unknown");
            if(user != null){
                record.setAuthor(user.getName());
            }
        }

        return Result.success(page);
    }
}
