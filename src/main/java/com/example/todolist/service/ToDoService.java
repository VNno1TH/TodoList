package com.example.todolist.service;

import com.example.todolist.entity.ToDo;
import com.example.todolist.entity.User;
import com.example.todolist.repository.ToDoRepository;
import com.example.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToDoService {
    private ToDoRepository toDoRepository;
    private UserRepository userRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository, UserRepository userRepository) {
        this.toDoRepository = toDoRepository;
        this.userRepository = userRepository;
    }

    public Page<ToDo> getTodosByUser(Long userId, Pageable pageable) {
        Optional<User> userOpt = userRepository.findUserById(userId);
        return userOpt.map(user -> toDoRepository.findAllByUser(user, pageable)).orElse(Page.empty());
    }

    public Optional<ToDo> getTodoById(Long id) {
        return toDoRepository.findById(id);
    }

    public Optional<ToDo> createTodo(Long userID, ToDo toDo) {
        Optional<User> userOpt = userRepository.findUserById(userID);
        if (!userOpt.isPresent()) return Optional.empty();
        toDo.setUser(userOpt.get());
        return Optional.of(toDoRepository.save(toDo));
    }

    public Optional<ToDo> updateTodo(Long id, ToDo updateTodo) {
        Optional<ToDo> toDoOpt = toDoRepository.findById(id);
        if (!toDoOpt.isPresent()) return Optional.empty();
        ToDo toDo = toDoOpt.get();
        toDo.setTitle(updateTodo.getTitle());
        toDo.setDesciption(updateTodo.getDesciption());
        toDo.setStatus(updateTodo.getStatus());
        toDo.setDueDate(updateTodo.getDueDate());
        toDo.setUpdatedAt(java.time.LocalDateTime.now());

        return Optional.of(toDoRepository.save(toDo));
    }

    public boolean deleteTodo(Long id) {
        if (!toDoRepository.existsById(id)) return false;
        toDoRepository.deleteById(id);
        return true;
    }
}