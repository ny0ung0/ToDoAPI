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
import com.clush.todolist.service.ToDoService;

@RestController
@RequestMapping("/api")
public class ToDoController {

	@Autowired
	private ToDoService todoService;

	// 할일 추가
	@PostMapping("/work")
	public ResponseEntity<?> addWork(@RequestBody ToDo todo) {
		
		todoService.addWork(todo);

		return ResponseEntity.ok().body("할 일 추가완료");
	}

	// 추가한 할일 리스트 출력
	@GetMapping("/work")
	public List<ToDo> geteWorkList() {
		
		List<ToDo> data = todoService.getWorkList();

		return data;
	}

	// 추가한 할일 수정
	@PutMapping("/work/{id}")
	public ResponseEntity<?> editWork(@PathVariable("id") Long id, @RequestBody ToDo todo) {
		
		ToDo updatedToDo = todoService.editWork(id, todo);
		return ResponseEntity.ok().body(updatedToDo);
	}

	// 추가한 할일 삭제
	@DeleteMapping("/work/{id}")
	public ResponseEntity<?> deleteWork(@PathVariable("id") Long id) {

		boolean isDeleted = todoService.deleteWork(id);
		if (isDeleted) {
			return ResponseEntity.ok().body("삭제 완료");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 ID가 존재하지 않습니다.");
		}
	}

	// 완료된 할일 리스트 출력
	@GetMapping("/work/completed")
	public List<ToDo> getCompletedWork() {
		
		return todoService.getCompleteWork();
	}

	// 미완료된 할일 리스트 출력
	@GetMapping("/work/pending")
	public List<ToDo> getPendingWork() {
		
		return todoService.getPendingWork();
	}

	// 중요도 별 할일 리스트 출력
	@GetMapping("/work/importance/{importance}")
	public List<ToDo> getWorkByPriority(@PathVariable("importance") Importance importance) {
		
		return todoService.getWorkByImportance(importance);
	}

	// 기한이 하루남은 할일 리스트 출력
	@GetMapping("/work/upcoming")
	public List<ToDo> getUpcomingWork() {
		
		return todoService.getUpcomingDeadlines();
	}

	// high -> medium -> low -> null 순으로 정렬된 작업 목록 반환
	@GetMapping("/work/desc")
	public ResponseEntity<List<ToDo>> getWorkListByImportanceDesc() {
		
		List<ToDo> sortedList = todoService.getWorkListByImportanceDesc();
		return ResponseEntity.ok(sortedList);
	}

	// low -> medium -> high -> null 순으로 정렬된 작업 목록 반환
	@GetMapping("/work/asc")
	public ResponseEntity<List<ToDo>> getWorkListByImportanceAsc() {
		
		List<ToDo> sortedList = todoService.getWorkListByImportanceAsc();
		return ResponseEntity.ok(sortedList);
	}

	// 오늘 포함 미래 날짜 작업 출력(오름차순)
	@GetMapping("/work/future")
	public ResponseEntity<List<ToDo>> getFutureAndTodayDueDateTasks() {
		
		List<ToDo> futureTasks = todoService.getFutureAndTodayDueDateTasks();
		return ResponseEntity.ok(futureTasks);
	}

	// 오늘 이전 날짜 작업 출력(내림차순)
	@GetMapping("/work/past")
	public ResponseEntity<List<ToDo>> getPastDueDateTasks() {
		
		List<ToDo> pastTasks = todoService.getPastDueDateTasks();
		return ResponseEntity.ok(pastTasks);
	}

	// 완료된 할 일 비율을 제공하는 API
	@GetMapping("/work/statistics")
	public ResponseEntity<Double> getCompletionPercentage() {
		
		double completionPercentage = todoService.calculateCompletionPercentage();
		return ResponseEntity.ok(completionPercentage);
	}

	// 특정 날짜의 완료 비율을 제공하는 API
	@GetMapping("/work/statistics/completion-percentage/date")
	public ResponseEntity<Double> getCompletionPercentageByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		
		double completionPercentage = todoService.calculateCompletionPercentageByDate(date);
		return ResponseEntity.ok(completionPercentage);
	}

	// 날짜 범위 내 완료 비율을 제공하는 API
	@GetMapping("/work/statistics/completion-percentage/range")
	public ResponseEntity<Double> getCompletionPercentageByDateRange(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		
		double completionPercentage = todoService.calculateCompletionPercentageByDateRange(startDate, endDate);
		return ResponseEntity.ok(completionPercentage);
	}

	// 주간 통계 API
	@GetMapping("/work/statistics/weekly")
	public ResponseEntity<?> getWeeklyStatistics(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
		
		double completionRate = todoService.getWeeklyCompletionRate(startDate);
		return ResponseEntity.ok("Completion Rate for the week: " + completionRate + "%");
	}

	// 월간 통계 API
	@GetMapping("/work/statistics/monthly")
	public ResponseEntity<?> getMonthlyStatistics(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
		
		double completionRate = todoService.getMonthlyCompletionRate(startDate);
		return ResponseEntity.ok("Completion Rate for the month: " + completionRate + "%");
	}
}
