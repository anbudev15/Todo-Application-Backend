package com.example.todo_backend.Repository;


import com.example.todo_backend.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByCompletedFalseAndDeletedFalseOrderByCreatedAtDesc();
    List<Todo> findByCompletedTrueAndDeletedFalseOrderByUpdatedAtDesc();
}