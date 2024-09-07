package com.clush.todolist.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clush.todolist.dto.Importance;
import com.clush.todolist.entity.ToDo;
import com.clush.todolist.repository.ToDoRepository;

@Service
public class ToDoService {

	@Autowired
	private ToDoRepository todoRepository;

	public void addWork(ToDo todo) {
		todo.setCompleted(false);

		todoRepository.save(todo);
	}

	public List<ToDo> getWorkList() {

		List<ToDo> data = new ArrayList<>();
		data = todoRepository.findAll();

		return data;
	}

	public ToDo editWork(Long id, ToDo todo) {
		Optional<ToDo> readyToDo = todoRepository.findById(id);
		if (readyToDo.isPresent()) {
			ToDo updateWork = readyToDo.get();
			if (todo.getTitle() != null) {
				updateWork.setTitle(todo.getTitle());
			}
			if (todo.getDescription() != null) {
				updateWork.setDescription(todo.getDescription());
			}
			if (todo.getCompleted() != null) {
				updateWork.setCompleted(todo.getCompleted());
			}
			if (todo.getImportance() != null) {
				updateWork.setImportance(todo.getImportance());
			}

			return todoRepository.save(updateWork);
		} else {
			throw new RuntimeException("해당 ID의 작업이 존재하지 않습니다.");
		}
	}

	public boolean deleteWork(Long id) {

		todoRepository.deleteById(id);
		return true;
	}

	public List<ToDo> getCompleteWork() {
		return todoRepository.findByCompleted(true);
	}

	public List<ToDo> getPendingWork() {
		return todoRepository.findByCompleted(false);
	}

	public List<ToDo> getWorkByImportance(Importance importance) {
		return todoRepository.findByImportance(importance);
	}

	public List<ToDo> getUpcomingDeadlines() {
		LocalDate now = LocalDate.now();
		LocalDate oneDayLater = now.plusDays(1);
		return todoRepository.findByDueDateBetween(now, oneDayLater);
	}

	public List<ToDo> getWorkListByImportanceDesc() {
		List<ToDo> data = todoRepository.findAll();

		data.sort(Comparator.comparing(ToDo::getImportance, Comparator.nullsLast(Comparator.reverseOrder())));

		return data;
	}

	public List<ToDo> getWorkListByImportanceAsc() {
		List<ToDo> data = todoRepository.findAll();

		data.sort(Comparator.comparing(ToDo::getImportance, Comparator.nullsLast(Comparator.naturalOrder())));

		return data;
	}

	public List<ToDo> getFutureAndTodayDueDateTasks() {
		LocalDate today = LocalDate.now();
		return todoRepository.findAll().stream()
				.filter(todo -> todo.getDueDate() != null && !todo.getDueDate().isBefore(today)) // 오늘 포함 이후 날짜
				.sorted((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate())) // 오름차순 정렬
				.collect(Collectors.toList());
	}

	public List<ToDo> getPastDueDateTasks() {
		LocalDate today = LocalDate.now();
		return todoRepository.findAll().stream()
				.filter(todo -> todo.getDueDate() != null && todo.getDueDate().isBefore(today)) // 오늘 이전 날짜
				.sorted((t1, t2) -> t2.getDueDate().compareTo(t1.getDueDate())) // 내림차순 정렬
				.collect(Collectors.toList());
	}

	public double calculateCompletionPercentage() {
		long totalTasks = todoRepository.count(); // 전체 할 일 개수
		long completedTasks = todoRepository.countByCompleted(true); // 완료된 할 일 개수

		if (totalTasks == 0) {
			return 0; // 할 일이 없을 경우 완료 비율 0%
		}

		return (double) completedTasks / totalTasks * 100; // 완료 비율 계산
	}

	public double calculateCompletionPercentageByDate(LocalDate date) {
		List<ToDo> tasksForDate = todoRepository.findByDueDate(date); // 해당 날짜의 할 일 목록
		long totalTasks = tasksForDate.size(); // 해당 날짜의 전체 할 일 개수
		long completedTasks = tasksForDate.stream().filter(ToDo::getCompleted) // Boolean 타입의 getter 메서드 사용
				.count();

		if (totalTasks == 0) {
			return 0;
		}

		return (double) completedTasks / totalTasks * 100; // 완료 비율 계산
	}

	// 날짜 범위별 완료 비율 계산 (예: 주간, 월간 통계)
	public double calculateCompletionPercentageByDateRange(LocalDate startDate, LocalDate endDate) {
		List<ToDo> tasksInRange = todoRepository.findByDueDateBetween(startDate, endDate); // 날짜 범위 내 할 일 목록
		long totalTasks = tasksInRange.size();
		long completedTasks = tasksInRange.stream().filter(ToDo::getCompleted).count();

		if (totalTasks == 0) {
			return 0;
		}

		return (double) completedTasks / totalTasks * 100;
	}

	// 주간 통계 계산
	public double getWeeklyCompletionRate(LocalDate startDate) {
		LocalDate endDate = startDate.plusDays(6); // 1주일 후까지 계산

		List<ToDo> tasksForWeek = todoRepository.findByDueDateBetween(startDate, endDate);
		long totalTasks = tasksForWeek.size();
		long completedTasks = tasksForWeek.stream().filter(ToDo::getCompleted).count();

		return totalTasks == 0 ? 0 : (completedTasks * 100.0 / totalTasks);
	}

	// 월간 통계 계산
	public double getMonthlyCompletionRate(LocalDate startDate) {
		LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()); // 해당 월의 마지막 날 계산
		List<ToDo> tasksForMonth = todoRepository.findByDueDateBetween(startDate, endDate);
		long totalTasks = tasksForMonth.size();
		long completedTasks = tasksForMonth.stream().filter(ToDo::getCompleted).count();

		return totalTasks == 0 ? 0 : (completedTasks * 100.0 / totalTasks);
	}
}
