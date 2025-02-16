/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 11:03 PM
 */
package com.example.springboottest.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboottest.GlobalException.ServiceException;
import com.example.springboottest.entity.User;
import com.example.springboottest.mapper.UserMapper;
import com.example.springboottest.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service    // To spring container
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource   // Save with Autowired, but won't scan the file
    UserMapper userMapper;

    @Override
    public boolean save(User entity) {  // Override parent method
        if (StrUtil.isBlank(entity.getName())) {
            entity.setName(entity.getUsername());
        }
        if (StrUtil.isBlank(entity.getPassword())) {
            entity.setPassword("123");
        }
        if (StrUtil.isBlank(entity.getRole())) {
            entity.setRole("user");
        }
        return super.save(entity);
    }

    public User selectByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); // Conditional query
        queryWrapper.eq("username", username);    // eq => == where username = #{username}

        return this.getOne(queryWrapper);    // SELECT * FROM user WHERE username = #{username}
    }

    // Need to validate username and password
    public User login(User user) {
        // Use username
        User dbUser = selectByUsername(user.getUsername());
        if (dbUser == null) {
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
        User dbUser = selectByUsername(user.getUsername());
        if (dbUser != null) {
            throw new ServiceException("Username already used");
        }
//        user.setName("SAM_BOOM-" + RandomUtil.randomNumbers(4));
        user.setName(user.getUsername());
        userMapper.insert(user);

        return user;
    }

    public void resetPassword(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if (dbUser == null) {
            // throw custom error message
            throw new ServiceException("Account does not exist!");
        }
        if (!user.getPhone().equals(dbUser.getPhone())) {
            throw new ServiceException("Incorrect information!");
        }
        dbUser.setPassword("123");
        updateById(dbUser);
    }
}
