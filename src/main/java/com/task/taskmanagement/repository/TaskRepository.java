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







// @Query("""
//     SELECT t FROM Task t
//     WHERE (:status IS NULL OR t.status = :status)
//       AND (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')))
//     ORDER BY t.dueDate ASC
// """)
// List<Task> findInitialTasks(
//         @Param("status") TaskStatus status,
//         @Param("search") String search,
//         Pageable pageable
// );




// @Query("""
//     SELECT t FROM Task t
//     WHERE (:status IS NULL OR t.status = :status)
//       AND (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')))
//       AND (:cursorDate IS NULL OR t.dueDate > :cursorDate)
//     ORDER BY t.dueDate ASC
// """)
// List<Task> findTasksWithCursor(
//         @Param("cursorDate") LocalDate cursorDate,
//         @Param("status") TaskStatus status,
//         @Param("search") String search,
//         Pageable pageable
// );


















    // findBy + <Field> + <Operation> + <Conditions> + OrderBy + <Field> + <Sort>
    

    // Page<Task> findByTitleContainingIgnoreCaseOrderByDueDateAsc(
    //         String title,
    //         Pageable pageable
    // );

    // Page<Task> findByStatusAndTitleContainingIgnoreCaseOrderByDueDateAsc(
    //         TaskStatus status,
    //         String title,
    //         Pageable pageable
    // );