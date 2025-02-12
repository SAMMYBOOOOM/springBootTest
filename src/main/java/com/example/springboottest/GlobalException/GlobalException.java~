/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/10/2025 5:20 PM
 */
package com.example.springboottest.GlobalException;

import com.example.springboottest.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result serviceException(ServiceException e){
        return Result.error("500", e.getMessage());
    }
}
