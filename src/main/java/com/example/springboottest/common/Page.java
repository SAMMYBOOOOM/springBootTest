/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/8/2025 11:39 PM
 */
package com.example.springboottest.common;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private Integer total;
    private List<T> list;
}
