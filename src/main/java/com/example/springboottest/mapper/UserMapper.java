package com.example.springboottest.mapper;

import com.example.springboottest.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper // Inject to Spring container
public interface UserMapper {

    @Insert("insert into user (username, password, name, phone, email, address, avatar)" +
            "values (#{username}, #{password}, #{name}, #{phone}, #{email}, #{address}, #{avatar});")
    void insert(User user);

    @Update("update user set username=#{username}, password=#{password}, name=#{name}, phone=#{phone}, " +
            "email=#{email}, address=#{address}, avatar=#{avatar} where id=#{id}")
    void updateUser(User user);

    @Delete("delete from user where id=#{id}")
    void deleteUser(Integer id);

    @Delete(("delete from user where id in (#{ids})"))
    void batchDeleteUser(List<Integer> ids);

    @Select("select * from user order by id desc")
    List<User> selectAll();

    @Select("select * from user where id=#{id} order by id desc")
    User selectById(Integer id);

    @Select("select * from user where name=#{name} order by id desc")
    List<User> selectByName(String name);

    @Select("select * from user where username=#{username} and name=#{name} order by id desc")
    List<User> selectByMul(@Param("username") String username, @Param("name") String name);

    @Select("select * from user where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%') order by id desc")
    List<User> selectByFuzzy(String username, String name);

    @Select("select * from user where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%') order by id desc limit #{skipNum}, #{pageSize}")
    List<User> selectByPage(Integer skipNum, Integer pageSize, String username, String name);

    @Select("select count(id) from user where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%')")
    int selectCountByPage(@Param("username") String username, @Param("name") String name);

    @Select("select * from user where username=#{username}")
    User selectByUsername(String username);
}
