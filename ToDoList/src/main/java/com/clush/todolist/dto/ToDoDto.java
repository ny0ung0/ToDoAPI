package com.clush.todolist.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ToDoDto {

	@Schema(description = "자동생성 되는 할 일 id & primary key")
	private Long id;

	@Schema(description = "할 일 제목", example="Clush에 지원하기")
	private String title;

	@Schema(description = "할 일 설명", example="사람인에서 Clush공고 확인 후 지원예정")
	private String description;

	@Schema(description = "완료 여부", example="false")
	private Boolean completed;
	
	@Schema(description = "중요도 설정 ex)high & medium & low", example="medium")
	private Importance importance;

	@Schema(description = "할 일 생성날짜 및 시간")
	private LocalDateTime createdAt;

	@Schema(description = "할 일 수정날짜 및 시간")
	private LocalDateTime updatedAt;
	
	@Schema(description = "할 일 마감 날짜")
	private LocalDate dueDate;
}
