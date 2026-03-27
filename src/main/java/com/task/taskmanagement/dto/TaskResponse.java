package com.task.taskmanagement.dto;

import java.time.LocalDate;

import com.task.taskmanagement.entity.TaskStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
}