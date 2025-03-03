/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/24/2025 9:06 PM
 */
package com.example.springboottest.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboottest.entity.Announcement;
import com.example.springboottest.entity.News;
import com.example.springboottest.mapper.AnnouncementMapper;
import com.example.springboottest.mapper.NewsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService extends ServiceImpl<AnnouncementMapper, Announcement> {

    @Resource
    AnnouncementMapper announcementMapper;
}
