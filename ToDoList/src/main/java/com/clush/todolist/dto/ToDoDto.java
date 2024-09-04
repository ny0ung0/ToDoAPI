package com.clush.todolist.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ToDoDto {

	private Long id;

	private String title;

	private String description;

	private boolean completed;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
