package com.clush.todolist.entity;

import com.clush.todolist.dto.Importance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ToDo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50, nullable = false)
	private String title;
	
	private String description;
	
	private Boolean completed;
	
	@Column(length = 6)
	@Enumerated(EnumType.STRING)
	private Importance importance;
	
	/*
	 * @ManyToOne
	 * @JoinColumn(name = "userid", nullable = false) 
	 * private Member userId;
	 */
}
