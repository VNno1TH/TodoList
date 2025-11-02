package com.example.todolist.repository;

import com.example.todolist.entity.ToDo;
import com.example.todolist.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Page<ToDo> findAllByUser(User user, Pageable pageable);

    Page<ToDo> findAllById(Long id,Pageable pageable);

}


