package com.example.todo_backend.Controller;

import com.example.todo_backend.Entity.Todo;
import com.example.todo_backend.Service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:4200") // adjust for production or use global CORS config
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Todo>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Todo>> active() {
        return ResponseEntity.ok(service.findActive());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Todo>> completed() {
        return ResponseEntity.ok(service.findCompleted());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) {
        Todo saved = service.create(todo);
        return ResponseEntity.created(URI.create("/api/todos/" + saved.getId())).body(saved);
    }

    /**
     * Partial update via service.
     * Accepts a JSON object with only the fields the client wants to change:
     * e.g. { "completed": true } or { "title": "New", "description": "x" }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return service.partialUpdate(id, updates).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        boolean ok = service.softDelete(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Permanent delete
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        boolean ok = service.hardDelete(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Restore
    @PostMapping("/{id}/restore")
    public ResponseEntity<Todo> restore(@PathVariable Long id) {
        return service.restore(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
