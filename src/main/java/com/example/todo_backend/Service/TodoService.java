package com.example.todo_backend.Service;

import com.example.todo_backend.Entity.Todo;
import com.example.todo_backend.Repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class TodoService {

    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    // Return all non-deleted todos (both active and completed)
    public List<Todo> findAll() {
        return repo.findAll().stream().filter(t -> !t.isDeleted()).toList();
    }

    // Return active (not completed, not deleted)
    public List<Todo> findActive() {
        return repo.findByCompletedFalseAndDeletedFalseOrderByCreatedAtDesc();
    }

    // Return completed (completed and not deleted)
    public List<Todo> findCompleted() {
        return repo.findByCompletedTrueAndDeletedFalseOrderByUpdatedAtDesc();
    }

    public Optional<Todo> findById(Long id) {
        return repo.findById(id).filter(t -> !t.isDeleted());
    }

    public Todo create(Todo todo) {
        todo.setId(null);
        if (todo.getTitle() == null) todo.setTitle("");
        todo.setDeleted(false);
        if (todo.getCreatedAt() == null) todo.setCreatedAt(java.time.OffsetDateTime.now());
        return repo.save(todo);
    }

    public Optional<Todo> update(Long id, Todo payload) {
        return repo.findById(id).map(existing -> {
            // full update: overwrite fields if provided (caller should provide all required fields)
            if (payload.getTitle() != null) existing.setTitle(payload.getTitle());
            if (payload.getDescription() != null) existing.setDescription(payload.getDescription());
            existing.setCompleted(payload.isCompleted());
            existing.setUpdatedAt(java.time.OffsetDateTime.now());
            return repo.save(existing);
        });
    }

    /**
     * Partial update / merge. Accepts a map of field -> value and updates only provided fields.
     * This mirrors PATCH-like behaviour but implemented as a safe PUT handler.
     */
    public Optional<Todo> partialUpdate(Long id, Map<String, Object> updates) {
        return repo.findById(id).map(existing -> {
            if (updates.containsKey("title")) {
                Object v = updates.get("title");
                existing.setTitle(v == null ? null : v.toString());
            }
            if (updates.containsKey("description")) {
                Object v = updates.get("description");
                existing.setDescription(v == null ? null : v.toString());
            }
            if (updates.containsKey("completed")) {
                Object v = updates.get("completed");
                boolean b = (v instanceof Boolean) ? (Boolean) v : Boolean.parseBoolean(String.valueOf(v));
                existing.setCompleted(b);
            }
            if (updates.containsKey("deleted")) {
                Object v = updates.get("deleted");
                boolean b = (v instanceof Boolean) ? (Boolean) v : Boolean.parseBoolean(String.valueOf(v));
                existing.setDeleted(b);
            }
            existing.setUpdatedAt(java.time.OffsetDateTime.now());
            return repo.save(existing);
        });
    }

    // soft-delete
    public boolean softDelete(Long id) {
        return repo.findById(id).map(t -> {
            t.setDeleted(true);
            t.setUpdatedAt(java.time.OffsetDateTime.now());
            repo.save(t);
            return true;
        }).orElse(false);
    }

    // hard delete
    public boolean hardDelete(Long id) {
        return repo.findById(id).map(t -> {
            repo.deleteById(id);
            return true;
        }).orElse(false);
    }

    public Optional<Todo> restore(Long id) {
        return repo.findById(id).map(t -> {
            t.setDeleted(false);
            // optionally set completed = false when restoring:
            t.setCompleted(false);
            t.setUpdatedAt(java.time.OffsetDateTime.now());
            return repo.save(t);
        });
    }
}
