package com.clush.todolist.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clush.todolist.dto.Importance;
import com.clush.todolist.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

	List<ToDo> findByCompleted(boolean completed);

	List<ToDo> findByImportance(Importance importance);

	List<ToDo> findByDueDateBetween(LocalDate now, LocalDate oneDayLater);
}
