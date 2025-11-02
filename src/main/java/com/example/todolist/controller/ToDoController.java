package com.example.todolist.controller;

import com.example.todolist.entity.ToDo;
import com.example.todolist.entity.User;
import com.example.todolist.service.ToDoService;
import com.example.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/todos")
public class ToDoController {
    private final ToDoService toDoService;
    private final UserService userService;

    @Autowired
    public ToDoController(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }

    // Lấy todos theo user - ADMIN xem được mọi user, USER chỉ xem của mình
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<ToDo>> getTodoByUser(@PathVariable Long id,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Optional<User> currentUser = userService.getUserByUsername(currentUsername);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // USER chỉ xem được todos của mình
        if (!isAdmin && currentUser.isPresent() && !currentUser.get().getId().equals(id)) {
            return ResponseEntity.status(403).build();
        }

        Page<ToDo> toDoPage = toDoService.getTodosByUser(id, PageRequest.of(page, size));
        return ResponseEntity.ok(toDoPage);
    }

    // Tạo todo - USER chỉ tạo cho mình, ADMIN tạo cho bất kỳ ai
    @PostMapping("/user/{userId}")
    public ResponseEntity<ToDo> createTodo(@PathVariable Long userId, @RequestBody ToDo todo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Optional<User> currentUser = userService.getUserByUsername(currentUsername);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // USER chỉ tạo todo cho mình
        if (!isAdmin && currentUser.isPresent() && !currentUser.get().getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        Optional<ToDo> newTodo = toDoService.createTodo(userId, todo);
        return newTodo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update todo - USER chỉ update todo của mình, ADMIN update mọi todo
    @PutMapping("/{todoId}")
    public ResponseEntity<ToDo> updateTodo(@PathVariable Long todoId, @RequestBody ToDo todo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Optional<User> currentUser = userService.getUserByUsername(currentUsername);
        Optional<ToDo> existingTodo = toDoService.getTodoById(todoId);

        if (!existingTodo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // USER chỉ update todo của mình
        if (!isAdmin && currentUser.isPresent() &&
                !existingTodo.get().getUser().getId().equals(currentUser.get().getId())) {
            return ResponseEntity.status(403).build();
        }

        Optional<ToDo> updated = toDoService.updateTodo(todoId, todo);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Xóa todo - USER chỉ xóa todo của mình, ADMIN xóa mọi todo
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Optional<User> currentUser = userService.getUserByUsername(currentUsername);
        Optional<ToDo> existingTodo = toDoService.getTodoById(todoId);

        if (!existingTodo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // USER chỉ xóa todo của mình
        if (!isAdmin && currentUser.isPresent() &&
                !existingTodo.get().getUser().getId().equals(currentUser.get().getId())) {
            return ResponseEntity.status(403).build();
        }

        boolean deleted = toDoService.deleteTodo(todoId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}