/**
 * Functionality:
 * Author: Sam Hsu
 * Date: 2/10/2025 5:22 PM
 */
package com.example.springboottest.GlobalException;

public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }
}
