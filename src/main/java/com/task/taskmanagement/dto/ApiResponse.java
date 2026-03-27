package com.task.taskmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private String status;   // SUCCESS / FAILURE
    private String message;
    private T data;
    private Object error;
}