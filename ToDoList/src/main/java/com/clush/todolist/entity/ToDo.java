package com.clush.todolist.entity;

import java.time.LocalDate;

import com.clush.todolist.dto.Importance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
	@Schema(description = "자동생성 되는 할 일 id & primary key")
	private Long id;
	
	@NotBlank
	@Column(length = 50, nullable = false)
	@Schema(description = "할 일 제목", example="Clush에 지원하기")
	private String title;
	
	@Schema(description = "할 일 설명", example="사람인에서 Clush공고 확인 후 지원예정")
	private String description;
	
	@Column(name = "completed", nullable = false)
	@Schema(description = "완료 여부", defaultValue = "false")
	private Boolean completed;
	
	
	@Column(length = 6)
	@Enumerated(EnumType.STRING)
	@Schema(description = "중요도 설정 ex)high & medium & low", example="medium", allowableValues = {"high", "medium", "low"})
	private Importance importance;
	
	@Column(name = "duedate", nullable = false)
	@Schema(description = "할 일 마감 날짜")
	private LocalDate dueDate;

}
