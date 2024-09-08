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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clush.todolist.dto.Importance;
import com.clush.todolist.entity.ToDo;
import com.clush.todolist.exception.NoContentException;
import com.clush.todolist.exception.NotFoundException;
import com.clush.todolist.service.ToDoService;

@RestController
@RequestMapping("/api")
public class ToDoController {

	@Autowired
	private ToDoService todoService;

	// 새로운 할 일을 추가하는 엔드포인트
	@PostMapping("/work")
	public ResponseEntity<String> addWork(@RequestBody ToDo todo) {

		todoService.addWork(todo);

		return ResponseEntity.ok().body("할 일 추가완료");
	}

	// 모든 할 일 목록을 반환
	@GetMapping("/work")
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
	public ResponseEntity<List<ToDo>> getCompletedWork() {

		return ResponseEntity.ok(todoService.getCompleteWork());
	}

	// 미완료된 할 일 목록을 반환
	@GetMapping("/work/pending")
	public ResponseEntity<List<ToDo>> getPendingWork() {

		return ResponseEntity.ok(todoService.getPendingWork());
	}

	// 중요도 별 할 일 목록을 반환
	@GetMapping("/work/importance/{importance}")
	public ResponseEntity<List<ToDo>> getWorkByPriority(@PathVariable("importance") Importance importance) {

		return ResponseEntity.ok(todoService.getWorkByImportance(importance));
	}

	// 기한이 하루 남은 할 일 목록을 반환
	@GetMapping("/work/upcoming")
	public ResponseEntity<List<ToDo>> getUpcomingWork() {

		return ResponseEntity.ok(todoService.getUpcomingDeadlines());
	}

	// 중요도 내림차순으로 정렬된 할 일 목록을 반환
	// high -> medium -> low -> null 순으로 정렬된 작업 목록 반환
	@GetMapping("/work/desc")
	public ResponseEntity<List<ToDo>> getWorkListByImportanceDesc() {

		return ResponseEntity.ok(todoService.getWorkListByImportanceDesc());
	}

	// 중요도 오름차순으로 정렬된 할 일 목록을 반환
	// low -> medium -> high -> null 순으로 정렬된 작업 목록 반환
	@GetMapping("/work/asc")
	public ResponseEntity<List<ToDo>> getWorkListByImportanceAsc() {

		return ResponseEntity.ok(todoService.getWorkListByImportanceAsc());
	}

	// 오늘과 미래 날짜의 기한을 가진 할 일 목록을 반환(오름차순)
	@GetMapping("/work/future")
	public ResponseEntity<List<ToDo>> getFutureAndTodayDueDateTasks() {
		return ResponseEntity.ok(todoService.getFutureAndTodayDueDateTasks());
	}

	// 오늘 이전 날짜의 기한을 가진 할 일 목록을 반환(내림차순)
	@GetMapping("/work/past")
	public ResponseEntity<List<ToDo>> getPastDueDateTasks() {

		return ResponseEntity.ok(todoService.getPastDueDateWork());
	}

	// 전체 할 일 중 완료된 할 일의 비율을 반환
	@GetMapping("/work/statistics")
	public ResponseEntity<Double> getCompletionPercentage() {

		double completionPercentage = todoService.calculateCompletionPercentage();
		return ResponseEntity.ok(completionPercentage);
	}

	// 특정 날짜의 완료 비율을 반환
	@GetMapping("/work/statistics/completion-percentage/date")
	public ResponseEntity<?> getCompletionPercentageByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		double completionPercentage = todoService.calculateCompletionPercentageByDate(date);
		return ResponseEntity.ok(completionPercentage + "%");
	}

	// 날짜 범위 내 완료 비율을 반환
	@GetMapping("/work/statistics/completion-percentage/range")
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
	public ResponseEntity<?> getWeeklyStatistics(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

		double completionRate = todoService.getWeeklyCompletionRate(startDate);
		return ResponseEntity.ok("Completion Rate for the week: " + completionRate + "%");
	}

	// 월간 완료 비율을 반환
	@GetMapping("/work/statistics/monthly")
	public ResponseEntity<?> getMonthlyStatistics(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
		double completionRate = todoService.getMonthlyCompletionRate(startDate);
		return ResponseEntity.ok("Completion Rate for the month: " + completionRate + "%");
	}
}
