package com.task.taskmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Map;

import com.task.taskmanagement.dto.ApiResponse;
import com.task.taskmanagement.dto.TaskRequest;
import com.task.taskmanagement.dto.TaskResponse;
import com.task.taskmanagement.entity.TaskStatus;
import com.task.taskmanagement.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService service;

    @InjectMocks
    private TaskController controller;

    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        taskRequest = new TaskRequest();
        taskRequest.setTitle("Task A");
        taskRequest.setDescription("desc");
        taskRequest.setDueDate(LocalDate.now().plusDays(5));
    }

    @Test
    void createShouldReturnSuccessResponse() {
        TaskResponse response = TaskResponse.builder()
                .id("task1")
                .title("Task A")
                .description("desc")
                .status(TaskStatus.PENDING)
                .dueDate(taskRequest.getDueDate())
                .build();

        when(service.create(any(TaskRequest.class))).thenReturn(response);

        ResponseEntity<ApiResponse<TaskResponse>> entity = controller.create(taskRequest);

        assertThat(entity.getStatusCode()).isNotNull();
        assertThat(entity.getStatusCode().value()).isEqualTo(200);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getStatus()).isEqualTo("SUCCESS");
        assertThat(entity.getBody().getData().getId()).isEqualTo("task1");
    }

    @Test
    void getShouldReturnTask() {
        TaskResponse response = TaskResponse.builder()
                .id("task1")
                .title("Task A")
                .description("desc")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(service.getById("task1")).thenReturn(response);

        ResponseEntity<ApiResponse<TaskResponse>> entity = controller.get("task1");

        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getData().getId()).isEqualTo("task1");
    }

    @Test
    void updateShouldReturnUpdatedTask() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated");

        TaskResponse response = TaskResponse.builder()
                .id("task1")
                .title("Updated")
                .description("desc")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(service.update(eq("task1"), any(TaskRequest.class))).thenReturn(response);

        ResponseEntity<ApiResponse<TaskResponse>> entity = controller.update("task1", request);

        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getData().getTitle()).isEqualTo("Updated");
    }

    @Test
    void deleteShouldReturnSuccess() {
        ResponseEntity<ApiResponse<Object>> entity = controller.delete("task1");

        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getStatus()).isEqualTo("SUCCESS");
        verify(service).delete("task1");
    }

    @Test
    void getAllShouldReturnSummary() {
        Map<String, Object> allResult = Map.of(
                "tasks", java.util.Collections.emptyList(),
                "currentPage", 0,
                "totalItems", 0L,
                "totalPages", 0
        );

        when(service.getAll(0, 5, null)).thenReturn(allResult);

        ResponseEntity<ApiResponse<Object>> entity = controller.getAll(0, 5, null);

        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getData()).isEqualTo(allResult);
    }
}
