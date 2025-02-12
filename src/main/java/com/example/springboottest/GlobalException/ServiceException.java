/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/10/2025 5:22 PM
 */
package com.example.springboottest.GlobalException;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final String code;

    public ServiceException(String message) {
        super(message);
        this.code = "500";
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
}
