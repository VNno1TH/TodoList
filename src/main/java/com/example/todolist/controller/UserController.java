package com.example.todolist.controller;

import com.example.todolist.entity.User;
import com.example.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Chỉ ADMIN mới xem được tất cả users
    @GetMapping("/allUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    // ADMIN xem được mọi user, USER chỉ xem được thông tin của mình
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Optional<User> currentUser = userService.getUserByUsername(currentUsername);
        Optional<User> targetUser = userService.getUserById(id);

        if (!targetUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // ADMIN có thể xem mọi user
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // USER chỉ xem được thông tin của mình
        if (!isAdmin && currentUser.isPresent() && !currentUser.get().getId().equals(id)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(targetUser.get());
    }

    // USER chỉ cập nhật được thông tin của mình, ADMIN cập nhật được mọi user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Optional<User> currentUser = userService.getUserByUsername(currentUsername);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // USER chỉ cập nhật được thông tin của mình
        if (!isAdmin && currentUser.isPresent() && !currentUser.get().getId().equals(id)) {
            return ResponseEntity.status(403).build();
        }

        Optional<User> user = userService.updateUser(id, updatedUser);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Chỉ ADMIN mới có quyền xóa user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}