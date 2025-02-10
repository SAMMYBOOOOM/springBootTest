/**
 * Functionality:   Return code for web * Author: Sam Hsu * Date: 2/4/2025 12:30 AM */
package com.example.springboottest.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    public static final String CODE_SUCCESS = "200";
    public static final String CODE_AUTH_ERROR = "401";
    public static final String CODE_SYSTEM_ERROR = "500";

    private String code;
    private String message;
    private Object data;

    public static Result success(){
        return Result.builder().code(CODE_SUCCESS).message("success").build();
    }

    public static Result success(Object data){
        return new Result(CODE_SUCCESS, "success", data);
    }

    public static Result error(String message){
        return new Result(CODE_SYSTEM_ERROR, message, null);
    }

    public static Result error(String code, String message){
        return new Result(code, message, null);
    }

    public static Result error(){
        return new Result(CODE_SYSTEM_ERROR, "System error", null);
    }
}