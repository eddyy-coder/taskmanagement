package com.task.taskmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.task.taskmanagement.entity.Task;
import com.task.taskmanagement.entity.TaskStatus;
public interface TaskRepository extends JpaRepository<Task, String> {

    Page<Task> findByStatusOrderByDueDateAsc(TaskStatus status, Pageable pageable);

    Page<Task> findAllByOrderByDueDateAsc(Pageable pageable);
}