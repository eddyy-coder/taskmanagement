package com.task.taskmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.task.taskmanagement.dto.TaskRequest;
import com.task.taskmanagement.dto.TaskResponse;
import com.task.taskmanagement.entity.Task;
import com.task.taskmanagement.entity.TaskStatus;
import com.task.taskmanagement.exception.ResourceNotFoundException;
import com.task.taskmanagement.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId("task1");
        task.setTitle("Test title");
        task.setDescription("Test description");
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(LocalDate.now().plusDays(7));
    }

    @Test
    void createShouldSaveTaskWithDefaultPendingStatusWhenStatusNull() {
        TaskRequest request = new TaskRequest();
        request.setTitle("New");
        request.setDescription("Desc");
        request.setDueDate(LocalDate.now().plusDays(10));

        Task savedTask = new Task();
        savedTask.setId("xyz");
        savedTask.setTitle(request.getTitle());
        savedTask.setDescription(request.getDescription());
        savedTask.setStatus(TaskStatus.PENDING);
        savedTask.setDueDate(request.getDueDate());

        when(repository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = service.create(request);

        assertThat(response.getId()).isEqualTo("xyz");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(response.getTitle()).isEqualTo("New");
        verify(repository).save(any(Task.class));
    }

    @Test
    void getByIdShouldReturnTaskResponseWhenFound() {
        when(repository.findById("task1")).thenReturn(Optional.of(task));

        TaskResponse response = service.getById("task1");

        assertThat(response.getId()).isEqualTo("task1");
        assertThat(response.getTitle()).isEqualTo("Test title");
    }

    @Test
    void getByIdShouldThrowExceptionWhenNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");
    }

    @Test
    void updateShouldOnlyApplyNonNullFields() {
        when(repository.findById(eq("task1"))).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskRequest update = new TaskRequest();
        update.setTitle("Updated");

        TaskResponse response = service.update("task1", update);

        assertThat(response.getTitle()).isEqualTo("Updated");
        assertThat(response.getDescription()).isEqualTo("Test description");
        verify(repository).save(any(Task.class));
    }

    @Test
    void updateShouldThrowExceptionWhenNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("missing", new TaskRequest()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");
    }

    @Test
    void deleteShouldThrowExceptionWhenMissing() {
        when(repository.existsById("missing")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(repository, never()).deleteById("missing");
    }

    @Test
    void deleteShouldCallRepositoryDeleteWhenExists() {
        when(repository.existsById("task1")).thenReturn(true);
        doNothing().when(repository).deleteById("task1");

        service.delete("task1");

        verify(repository).deleteById("task1");
    }

    @Test
    void getAllShouldReturnStandardMapWhenStatusNull() {
        Pageable pageRequest = PageRequest.of(0, 5);
        Page<Task> page = new PageImpl<>(List.of(task), pageRequest, 1);

        when(repository.findAllByOrderByDueDateAsc(pageRequest)).thenReturn(page);

        Map<String, Object> result = service.getAll(0, 5, null);

        assertThat(result.get("tasks")).asList().hasSize(1);
        assertThat(result.get("totalItems")).isEqualTo(1L);
    }

    @Test
    void getAllShouldReturnFilteredTasksWhenStatusProvided() {
        Pageable pageRequest = PageRequest.of(0, 5);
        Page<Task> page = new PageImpl<>(List.of(task), pageRequest, 1);

        when(repository.findByStatusOrderByDueDateAsc(TaskStatus.PENDING, pageRequest)).thenReturn(page);

        Map<String, Object> result = service.getAll(0, 5, "PENDING");

        assertThat(result.get("tasks")).asList().hasSize(1);
        assertThat(result.get("totalItems")).isEqualTo(1L);
    }
}
