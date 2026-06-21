package com.project.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import com.project.todo.dto.TodoRequest;
import com.project.todo.dto.TodoUpdateRequest;
import com.project.todo.entity.Todo;
import com.project.todo.service.TodoService;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<Page<Todo>> getMyTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(todoService.getUserTodos(getCurrentUserEmail(), pageable));
    }

    @PostMapping

    public ResponseEntity<Todo> createTodo(@Valid @RequestBody TodoRequest request) {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(todoService.createTodo(request.title(), email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoUpdateRequest request) {
        return ResponseEntity.ok(todoService.updateTodo(id, request.title(), request.status(), getCurrentUserEmail()));
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id, getCurrentUserEmail());
        return ResponseEntity.ok("Todo deleted Successfully");
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
