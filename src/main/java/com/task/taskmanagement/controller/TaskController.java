package com.task.taskmanagement.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task.taskmanagement.dto.ApiResponse;
import com.task.taskmanagement.dto.TaskRequest;
import com.task.taskmanagement.dto.TaskResponse;
import com.task.taskmanagement.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> create(@Valid @RequestBody TaskRequest request) {
        TaskResponse response = service.create(request);

        return ResponseEntity.ok(
                ApiResponse.<TaskResponse>builder()
                        .status("SUCCESS")
                        .message("Task created successfully")
                        .data(response)
                        .error(null)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> get(@PathVariable String id) {
        TaskResponse response = service.getById(id);

        return ResponseEntity.ok(
                ApiResponse.<TaskResponse>builder()
                        .status("SUCCESS")
                        .message("Task fetched successfully")
                        .data(response)
                        .error(null)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> update(@PathVariable String id,
            @RequestBody TaskRequest request) {

        TaskResponse response = service.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.<TaskResponse>builder()
                        .status("SUCCESS")
                        .message("Task updated successfully")
                        .data(response)
                        .error(null)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String id) {
        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("SUCCESS")
                        .message("Task deleted successfully")
                        .data(null)
                        .error(null)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String status
    ) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("SUCCESS")
                        .message("Tasks fetched successfully")
                        .data(service.getAll(page, size, status))
                        .error(null)
                        .build()
        );
    }
}
























// @GetMapping
// public ResponseEntity<ApiResponse<Object>> getAll(
//         @RequestParam(defaultValue = "5") int size,
//         @RequestParam(required = false) String status,
//         @RequestParam(required = false) String search,
//         @RequestParam(required = false) String cursor   // 👈 NEW
// ) {

//     return ResponseEntity.ok(
//             ApiResponse.builder()
//                     .status("SUCCESS")
//                     .message("Tasks fetched successfully")
//                     .data(service.getAllWithCursor(size, status, search, cursor))
//                     .error(null)
//                     .build()
//     );
// }
