/**
 * Functionality:   Data retrieve
 * Author: Sam Hsu
 * Date: 2/4/2025 12:18 AM
 */
package com.example.springboottest.controller;

import com.example.springboottest.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web")
public class WebController {

    @RequestMapping(value = "/hello", method = RequestMethod.POST)  // /web/hello
    public Result hello(){
        return Result.success("Hello world success!");
    }

    @PostMapping(value = "/post")  // /web/post
    public Result post(@RequestBody Obj obj){
        System.out.println(obj.getName() == null ? "Is null not empty" : obj.getName().isEmpty());
        return Result.success(obj);
    }

    @PutMapping(value = "/put")  // /web/put
    public Result put(@RequestBody Obj obj){
        return Result.success(obj);
    }

    @DeleteMapping(value = "/delete/{id}")  // /web/delete
    public Result delete(@PathVariable Integer id){
        return Result.success(id);
    }
}
