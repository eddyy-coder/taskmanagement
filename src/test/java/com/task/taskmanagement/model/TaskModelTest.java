package com.task.taskmanagement.model;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.task.taskmanagement.dto.TaskRequest;
import com.task.taskmanagement.dto.TaskResponse;
import com.task.taskmanagement.entity.Task;
import com.task.taskmanagement.entity.TaskStatus;

class TaskModelTest {

    @Test
    void taskEntitySettersAndGettersShouldWork() {
        Task task = new Task();
        task.setId("123");
        task.setTitle("Write tests");
        task.setDescription("Write unit tests for all components");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDueDate(LocalDate.now().plusDays(3));

        assertThat(task.getId()).isEqualTo("123");
        assertThat(task.getTitle()).isEqualTo("Write tests");
        assertThat(task.getDescription()).isEqualTo("Write unit tests for all components");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getDueDate()).isEqualTo(LocalDate.now().plusDays(3));
    }

    @Test
    void taskEntityGenerateIdPrePersistShouldSetId() {
        Task task = new Task();

        task.generateId();

        assertThat(task.getId()).isNotBlank();
    }

    @Test
    void taskStatusEnumShouldContainTheExpectedValues() {
        assertThat(TaskStatus.values()).containsExactly(TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.DONE);
    }

    @Test
    void taskRequestAndResponseDtoShouldHoldValues() {
        TaskRequest request = new TaskRequest();
        request.setTitle("New task");
        request.setDescription("Task description");
        request.setStatus(TaskStatus.PENDING);
        request.setDueDate(LocalDate.now().plusDays(5));

        assertThat(request.getTitle()).isEqualTo("New task");
        assertThat(request.getDescription()).isEqualTo("Task description");
        assertThat(request.getStatus()).isEqualTo(TaskStatus.PENDING);

        TaskResponse response = TaskResponse.builder()
                .id("abc")
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .build();

        assertThat(response.getId()).isEqualTo("abc");
        assertThat(response.getTitle()).isEqualTo("New task");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.PENDING);
    }
}