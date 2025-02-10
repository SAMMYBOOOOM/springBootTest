/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 11:03 PM
 */
package com.example.springboottest.service;

import com.example.springboottest.common.Page;
import com.example.springboottest.entity.User;
import com.example.springboottest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service    // To spring container
public class UserService {

    @Autowired
    UserMapper userMapper;
    public void insertUser(User user) {
        userMapper.insert(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    public void batchDeleteUser(List<Integer> ids) {
        for(Integer id : ids) {
            userMapper.deleteUser(id);
        }

//        userMapper.batchDeleteUser(ids);
    }

    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectByName(String name) {
        return userMapper.selectByName(name);
    }

    public List<User> selectByMul(String username, String name) {
        return userMapper.selectByMul(username, name);
    }

    public List<User> selectByFuzzy(String username, String name) {
        return userMapper.selectByFuzzy(username, name);
    }

    public Page<User> selectByPage(Integer pageNum, Integer pageSize, String username, String name) {
        Integer skipNum = (pageNum - 1) * pageSize; // Skip calculation

        Page<User> page = new Page<>();
        Map<String, Object> result = new HashMap<>();
        List<User> userList = userMapper.selectByPage(skipNum, pageSize, username, name);
        Integer total = userMapper.selectCountByPage(username, name);
        page.setTotal(total);
        page.setList(userList);
        return page;
    }
}
