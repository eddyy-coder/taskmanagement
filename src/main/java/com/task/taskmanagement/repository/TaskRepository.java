package com.task.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.taskmanagement.entity.Task;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findAllByOrderByDueDateAsc();
}