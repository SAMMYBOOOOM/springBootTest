/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/12/2025 6:37 PM
 */
package com.example.springboottest.controller;

import cn.hutool.core.io.FileUtil;
import com.example.springboottest.common.AuthAccess;
import com.example.springboottest.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${ip:localhost}")   // name:(default)
    String ip;
    @Value("${port:9090}")
    String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";  // Storage path

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String mainName = FileUtil.mainName(originalFilename);
        String extName = FileUtil.extName(originalFilename);
        if (!FileUtil.exist(ROOT_PATH)) {
            FileUtil.mkdir(ROOT_PATH);    // Create a directory if the parent directory does not exist
        }
        if (FileUtil.exist(ROOT_PATH + File.separator + originalFilename)) {  // if the file already exists, rename it
            originalFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;
        }
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        file.transferTo(saveFile);
        String url = "http://" + ip + ":" + port + "/file/download/" + originalFilename;
        return Result.success(url); // Return the download address
    }

    @AuthAccess
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        String filePath = ROOT_PATH + File.separator + fileName;
        if(!FileUtil.exist(filePath)){
            return;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);  // Array, also the file stream data
        outputStream.flush();
        outputStream.close();
    }
}
