package com.task.taskmanagement.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private String id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
}

































// @Table(
//     indexes = {
//         @Index(name = "idx_title", columnList = "title")
//     }
// )