package com.clush.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clush.todolist.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

}
