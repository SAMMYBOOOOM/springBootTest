/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/24/2025 9:01 PM
 */
package com.example.springboottest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class News {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private String content;
    private Integer authorid;
    private String date;

    // This field does not exist in the database
    @TableField(exist = false)
    private String author;

}
