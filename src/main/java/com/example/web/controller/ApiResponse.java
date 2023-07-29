package com.example.web.controller;


import com.example.model.Cricketer;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResponse {
    private int status;
    private String message;
    private List<Cricketer> data;

    public ApiResponse(int status, String message, List<Cricketer> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


}
