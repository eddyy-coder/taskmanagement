package com.task.taskmanagement.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.task.taskmanagement.dto.TaskRequest;
import com.task.taskmanagement.dto.TaskResponse;
import com.task.taskmanagement.entity.Task;
import com.task.taskmanagement.entity.TaskStatus;
import com.task.taskmanagement.exception.ResourceNotFoundException;
import com.task.taskmanagement.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING);
        task.setDueDate(request.getDueDate());

        return mapToResponse(repository.save(task));
    }

    public TaskResponse getById(String id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return mapToResponse(task);
    }

    public TaskResponse update(String id, TaskRequest request) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        return mapToResponse(repository.save(task));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }
        repository.deleteById(id);
    }

    public Map<String, Object> getAll(int page, int size, String status) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage;

        if (status != null) {
            taskPage = repository.findByStatusOrderByDueDateAsc(
                    TaskStatus.valueOf(status.toUpperCase()), pageable);
        } else {
            taskPage = repository.findAllByOrderByDueDateAsc(pageable);
        }

        return Map.of(
                "tasks", taskPage.getContent().stream().map(this::mapToResponse).toList(),
                "currentPage", taskPage.getNumber(),
                "totalItems", taskPage.getTotalElements(),
                "totalPages", taskPage.getTotalPages()
        );
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build();
    }
}
