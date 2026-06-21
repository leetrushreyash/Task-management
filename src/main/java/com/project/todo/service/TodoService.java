package com.project.todo.service;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.project.todo.entity.Todo;
import com.project.todo.entity.UserEntry;
import com.project.todo.enums.StatusEnum;
import com.project.todo.exception.OwnershipException;
import com.project.todo.repository.TodoRepository;
import com.project.todo.repository.UserRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public Todo createTodo(String title, String email) {
        Todo todo = new Todo();
        UserEntry user = userRepository.findByEmail(email).orElseThrow();
        todo.setTitle(title);
        todo.setUserid(user);
        todo.setStatus(StatusEnum.PENDING);
        return todoRepository.save(todo);
    }

    public Page<Todo> getUserTodos(String email, Pageable pageable) {
        return todoRepository.findAllByUser(userRepository.findByEmail(email).orElse(null), pageable);
    }

    public void deleteTodo(Long todoId, String email) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new RuntimeException("todo not found"));
        if (!todo.getUserid().getEmail().equals(email)) {
            throw new OwnershipException("Access Denied : You do not own this todo");
        }

        todoRepository.delete(todo);
    }

    public Todo updateTodo(Long todoId, String title, StatusEnum status, String email) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new RuntimeException("todo not found"));
        if (!todo.getUserid().getEmail().equals(email)) {
            throw new OwnershipException("Access Denied : You do not own this todo");
        }

        if (title != null && !title.trim().isEmpty()) {
            todo.setTitle(title);
        }
        if (status != null) {
            todo.setStatus(status);
        }

        return todoRepository.save(todo);
    }

}
