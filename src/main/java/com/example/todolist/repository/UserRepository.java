package com.example.todolist.repository;

import com.example.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long id);

    boolean existsUserByUsername(String username);

    Optional<User> getUsersById(Long id);
}


