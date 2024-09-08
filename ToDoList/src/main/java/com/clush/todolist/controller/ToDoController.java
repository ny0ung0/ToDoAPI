package com.clush.todolist.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.clush.todolist.dto.Importance;
import com.clush.todolist.entity.ToDo;
import com.clush.todolist.exception.NoContentException;
import com.clush.todolist.exception.NotFoundException;
import com.clush.todolist.service.ToDoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "ToDo Controller", description = "할 일 crud, 정렬, 통계 API")
public class ToDoController {

	@Autowired
	private ToDoService todoService;

	// 새로운 할 일을 추가하는 엔드포인트
	@PostMapping("/work")
	@Operation(summary = "새로운 할 일 추가", description = "title은 필수로 입력해야 합니다. dueDate는 입력하지 않는 경우 오늘 날짜로 저장됩니다.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ToDo.class), examples = @ExampleObject(value = "{ \"title\": \"할 일 제목\", \"description\": \"할 일 설명\", \"importance\": \"medium\"}"))))
	public ResponseEntity<String> addWork(@RequestBody ToDo todo) {

		todoService.addWork(todo);

		return ResponseEntity.ok().body("할 일 추가완료");
	}

	// 모든 할 일 목록을 반환
	@GetMapping("/work")
	@Operation(summary = "모든 할 일 목록 반환")
	public ResponseEntity<?> getWorkList() {
		try {
			List<ToDo> data = todoService.getWorkList();
			return ResponseEntity.ok(data);
		} catch (NoContentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// 특정 ID의 할 일을 수정
	@PutMapping("/work/{id}")
	@Operation(summary = "특정 ID의 할 일을 수정", description = "할 일의 id를 지정하고 completed를 true로 변경해보세요.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ToDo.class), examples = @ExampleObject(value = "{ \"title\": \"clush 지원하기\", \"description\": \"사람인으로 clush 공고 지원\", \"importance\": \"high\", \"completed\": \"true\"}"))))
	public ResponseEntity<?> editWork(@PathVariable("id") Long id, @RequestBody ToDo todo) {
		try {
			ToDo updatedToDo = todoService.editWork(id, todo);
			return ResponseEntity.ok(updatedToDo);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// 특정 ID의 할 일을 삭제
	@DeleteMapping("/work/{id}")
	@Operation(summary = "특정 ID의 할 일을 삭제")
	public ResponseEntity<String> deleteWork(@PathVariable("id") Long id) {
		try {
			todoService.deleteWork(id);
			return ResponseEntity.ok().body("삭제 완료");
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// 완료된 할 일 목록을 반환
	@GetMapping("/work/completed")
	@Operation(summary = "완료된 할 일 목록을 반환")
	public ResponseEntity<List<ToDo>> getCompletedWork() {

		return ResponseEntity.ok(todoService.getCompleteWork());
	}

	// 미완료된 할 일 목록을 반환
	@GetMapping("/work/pending")
	@Operation(summary = "미완료된 할 일 목록을 반환")
	public ResponseEntity<List<ToDo>> getPendingWork() {

		return ResponseEntity.ok(todoService.getPendingWork());
	}

	// 중요도 별 할 일 목록을 반환
	@GetMapping("/work/importance/{importance}")
	@Operation(summary = "중요도 별 할 일 목록을 반환")
	public ResponseEntity<List<ToDo>> getWorkByPriority(@PathVariable("importance") Importance importance) {

		return ResponseEntity.ok(todoService.getWorkByImportance(importance));
	}

	// 기한이 하루 남은 할 일 목록을 반환
	@GetMapping("/work/upcoming")
	@Operation(summary = "기한이 하루 남은 할 일 목록을 반환")
	public ResponseEntity<List<ToDo>> getUpcomingWork() {

		return ResponseEntity.ok(todoService.getUpcomingDeadlines());
	}

	// 중요도 내림차순으로 정렬된 할 일 목록을 반환
	// high -> medium -> low -> null 순으로 정렬된 작업 목록 반환
	@GetMapping("/work/desc")
	@Operation(summary = "중요도 내림차순으로 정렬된 할 일 목록을 반환", description = "high -> medium -> low -> null 순으로 정렬된 작업 목록 반환")
	public ResponseEntity<List<ToDo>> getWorkListByImportanceDesc() {

		return ResponseEntity.ok(todoService.getWorkListByImportanceDesc());
	}

	// 중요도 오름차순으로 정렬된 할 일 목록을 반환
	// low -> medium -> high -> null 순으로 정렬된 작업 목록 반환
	@GetMapping("/work/asc")
	@Operation(summary = "중요도 오름차순으로 정렬된 할 일 목록을 반환", description = "low -> medium -> high -> null 순으로 정렬된 작업 목록 반환")
	public ResponseEntity<List<ToDo>> getWorkListByImportanceAsc() {

		return ResponseEntity.ok(todoService.getWorkListByImportanceAsc());
	}

	// 오늘과 미래 날짜의 기한을 가진 할 일 목록을 반환(오름차순)
	@GetMapping("/work/future")
	@Operation(summary = "오늘과 미래 날짜의 기한을 가진 할 일 목록을 반환(오름차순)")
	public ResponseEntity<List<ToDo>> getFutureAndTodayDueDateTasks() {
		return ResponseEntity.ok(todoService.getFutureAndTodayDueDateTasks());
	}

	// 오늘 이전 날짜의 기한을 가진 할 일 목록을 반환(내림차순)
	@GetMapping("/work/past")
	@Operation(summary = "오늘 이전 날짜의 기한을 가진 할 일 목록을 반환(내림차순)")
	public ResponseEntity<List<ToDo>> getPastDueDateTasks() {

		return ResponseEntity.ok(todoService.getPastDueDateWork());
	}

	// 전체 할 일 중 완료된 할 일의 비율을 반환
	@GetMapping("/work/statistics")
	@Operation(summary = "전체 할 일 중 완료된 할 일의 비율을 반환")
	public ResponseEntity<?> getCompletionPercentage() {

		double completionPercentage = todoService.calculateCompletionPercentage();
		return ResponseEntity.ok(completionPercentage + "%");
	}

	// 특정 날짜의 완료 비율을 반환
	@GetMapping("/work/statistics/completion-percentage/date")
	@Operation(summary = "특정 날짜의 완료 비율을 반환")
	@Parameter(name = "date", description = "dueDate", example = "2024-09-09")
	public ResponseEntity<?> getCompletionPercentageByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		double completionPercentage = todoService.calculateCompletionPercentageByDate(date);
		return ResponseEntity.ok(completionPercentage + "%");
	}

	// 특정 날짜 범위 내 완료 비율을 반환
	@GetMapping("/work/statistics/completion-percentage/range")
	@Operation(summary = "특정 날짜 범위 내 완료 비율을 반환")
	@Parameters({ @Parameter(name = "startDate", description = "통계 범위 시작날짜 dueDate", example = "2024-09-04"),
			@Parameter(name = "endDate", description = "통계 범위 마지막날짜 dueDate", example = "2024-09-22") })
	public ResponseEntity<?> getCompletionPercentageByDateRange(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		try {
			// 서비스 호출
			double completionPercentage = todoService.calculateCompletionPercentageByDateRange(startDate, endDate);
			return ResponseEntity.ok(completionPercentage + "%");
		} catch (IllegalArgumentException e) {
			// 날짜 검증 실패 시 오류 메시지 반환
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// 주간 완료 비율을 반환
	@GetMapping("/work/statistics/weekly")
	@Operation(summary = "주간 완료 비율을 반환")
	@Parameter(name = "startDate", description = "주간 통계 시작날짜 dueDate", example = "2024-09-09")
	public ResponseEntity<?> getWeeklyStatistics(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

		double completionRate = todoService.getWeeklyCompletionRate(startDate);
		return ResponseEntity.ok("Completion Rate for the week: " + completionRate + "%");
	}

	// 월간 완료 비율을 반환
	@GetMapping("/work/statistics/monthly")
	@Operation(summary = "월간 완료 비율을 반환")
	@Parameter(name = "startDate", description = "주간 통계 시작날짜 dueDate", example = "2024-09-01")
	public ResponseEntity<?> getMonthlyStatistics(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

		double completionRate = todoService.getMonthlyCompletionRate(startDate);
		return ResponseEntity.ok("Completion Rate for the month: " + completionRate + "%");
	}
}
