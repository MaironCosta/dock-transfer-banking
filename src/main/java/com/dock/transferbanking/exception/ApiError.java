package com.dock.transferbanking.exception;


import lombok.Data;

import java.util.Map;

@Data
public class ApiError  {

    private final String result;

    public ApiError(String message) {
        this.result = message;
    }

}