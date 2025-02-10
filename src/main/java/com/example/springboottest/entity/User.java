/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/7/2025 10:47 PM
 */
package com.example.springboottest.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String avatar;


}
