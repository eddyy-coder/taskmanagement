package com.task.taskmanagement.dto;

import java.time.LocalDate;

import com.task.taskmanagement.entity.TaskStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {

    @NotNull(message = "Title is required")
    private String title;

    private String description;

    private TaskStatus status;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;
}
