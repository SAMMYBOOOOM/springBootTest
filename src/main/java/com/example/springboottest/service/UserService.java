/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 11:03 PM
 */
package com.example.springboottest.service;

import cn.hutool.core.util.RandomUtil;
import com.example.springboottest.GlobalException.ServiceException;
import com.example.springboottest.common.Page;
import com.example.springboottest.entity.User;
import com.example.springboottest.mapper.UserMapper;
import com.example.springboottest.utils.TokenUtils;
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

    // Need to validate username and password
    public User login(User user) {
        // Use username
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser == null){
            // Output custom error message
            throw new ServiceException("Account does not exist!");
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException("Incorrect password!");
        }
        // Create token
        String token = TokenUtils.createToken(dbUser.getId().toString(), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public User register(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser != null){
            throw new ServiceException("Username already used");
        }
//        user.setName("SAM_BOOM-" + RandomUtil.randomNumbers(4));
        user.setName(user.getUsername());
        userMapper.insert(user);

        return user;
    }

    public void resetPassword(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser == null){
            // throw custom error message
            throw new ServiceException("Account does not exist!");
        }
        if(!user.getPhone().equals(dbUser.getPhone())) {
            throw new ServiceException("Incorrect information!");
        }
        dbUser.setPassword("123");
        userMapper.updateUser(dbUser);
    }
}
