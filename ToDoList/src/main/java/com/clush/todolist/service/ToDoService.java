package com.clush.todolist.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clush.todolist.dto.Importance;
import com.clush.todolist.entity.ToDo;
import com.clush.todolist.exception.NoContentException;
import com.clush.todolist.exception.NotFoundException;
import com.clush.todolist.repository.ToDoRepository;
import com.clush.todolist.utility.StatisticsFormatUtils;

@Service
public class ToDoService {

	@Autowired
	private ToDoRepository todoRepository;

	// 새로운 할 일 추가
	public void addWork(ToDo todo) {
		todo.setCompleted(false);

		todoRepository.save(todo);
	}

	// 모든 할 일 목록을 반환
	public List<ToDo> getWorkList() {
		List<ToDo> data = todoRepository.findAll();
		if (data.isEmpty()) {
			throw new NoContentException("할 일이 없습니다."); // 데이터가 없을 때 예외처리
		}
		return data;
	}

	// 특정 ID의 할 일을 수정
	public ToDo editWork(Long id, ToDo todo) {
		ToDo updateWork = todoRepository.findById(id).orElseThrow(() -> new NotFoundException("해당 ID의 작업이 존재하지 않습니다."));

		// 수정할 필드가 존재하면 업데이트
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
		if (todo.getDueDate() != null) {
			updateWork.setDueDate(todo.getDueDate());
		}

		return todoRepository.save(updateWork);
	}

	// 특정 ID의 할 일을 삭제
	public boolean deleteWork(Long id) {
		if (!todoRepository.existsById(id)) {
			throw new NotFoundException("해당 ID가 존재하지 않습니다.");
		}
		todoRepository.deleteById(id);
		return true;
	}

	// 완료된 할 일 목록을 반환
	public List<ToDo> getCompleteWork() {

		return todoRepository.findByCompleted(true);
	}

	// 미완료된 할 일 목록을 반환
	public List<ToDo> getPendingWork() {

		return todoRepository.findByCompleted(false);
	}

	// 중요도 별 할 일 목록을 반환
	public List<ToDo> getWorkByImportance(Importance importance) {

		return todoRepository.findByImportance(importance);
	}

	// 기한이 하루 남은 할 일 목록을 반환
	public List<ToDo> getUpcomingDeadlines() {

		LocalDate now = LocalDate.now();
		LocalDate oneDayLater = now.plusDays(1);
		return todoRepository.findByDueDateBetween(now, oneDayLater);
	}

	// 중요도 내림차순으로 정렬된 할 일 목록을 반환
	public List<ToDo> getWorkListByImportanceDesc() {

		List<ToDo> data = todoRepository.findAll();

		data.sort(Comparator.comparing(ToDo::getImportance, Comparator.nullsLast(Comparator.reverseOrder())));

		return data;
	}

	// 중요도 오름차순으로 정렬된 할 일 목록을 반환
	public List<ToDo> getWorkListByImportanceAsc() {

		List<ToDo> data = todoRepository.findAll();

		data.sort(Comparator.comparing(ToDo::getImportance, Comparator.nullsLast(Comparator.naturalOrder())));

		return data;
	}

	// 오늘과 미래 날짜의 기한을 가진 할 일 목록을 반환
	public List<ToDo> getFutureAndTodayDueDateTasks() {
		LocalDate today = LocalDate.now();
		return todoRepository.findByDueDateGreaterThanEqualOrderByDueDate(today);
	}

	// 오늘 이전 날짜의 기한을 가진 할 일 목록을 반환
	public List<ToDo> getPastDueDateWork() {
		LocalDate today = LocalDate.now();
		List<ToDo> tasks = todoRepository.findAll(); // 이미 한 번에 데이터를 가져옴
		tasks.removeIf(todo -> todo.getDueDate() == null || !todo.getDueDate().isBefore(today));
		tasks.sort(Comparator.comparing(ToDo::getDueDate).reversed());
		return tasks;
	}

	// 전체 할 일 중 완료된 할 일의 비율을 계산
	public double calculateCompletionPercentage() {

		long totalTasks = todoRepository.count(); // 전체 할 일 개수

		if (totalTasks == 0) {
			return 0; // 할 일이 없을 경우 완료 비율 0%
		}

		long completedTasks = todoRepository.countByCompleted(true); // 완료된 할 일 개수
		double percentage = (double) completedTasks / totalTasks * 100; // 완료 비율 계산

		return StatisticsFormatUtils.format(percentage);
	}

	// 특정 날짜의 완료 비율을 계산
	public double calculateCompletionPercentageByDate(LocalDate date) {

		List<ToDo> tasksForDate = todoRepository.findByDueDate(date); // 해당 날짜의 할 일 목록
		long totalTasks = tasksForDate.size(); // 해당 날짜의 전체 할 일 개수
		long completedTasks = tasksForDate.stream().filter(ToDo::getCompleted) // Boolean 타입의 getter 메서드 사용
				.count();

		if (totalTasks == 0) {
			return 0;
		}
		double percentage = (double) completedTasks / totalTasks * 100; // 완료 비율 계산
		return StatisticsFormatUtils.format(percentage);
	}

	// 날짜 범위 내 완료 비율을 계산
	public double calculateCompletionPercentageByDateRange(LocalDate startDate, LocalDate endDate) {

		// 날짜 검증: 종료일이 시작일보다 이전일 경우 예외 발생
		if (endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("종료일은 시작일보다 이전일 수 없습니다."); // 종료일이 시작일보다 이전일 경우 예외 처리
		}

		// 날짜 범위 내 할 일 목록
		List<ToDo> tasksInRange = todoRepository.findByDueDateBetween(startDate, endDate);
		long totalTasks = tasksInRange.size();
		long completedTasks = tasksInRange.stream().filter(ToDo::getCompleted).count();

		// 할 일이 없을 경우 0% 반환
		if (totalTasks == 0) {
			return 0;
		}

		double percentage = (double) completedTasks / totalTasks * 100; // 완료 비율 계산
		return StatisticsFormatUtils.format(percentage);
	}

	// 주간 완료 비율을 계산
	public double getWeeklyCompletionRate(LocalDate startDate) {
		LocalDate endDate = startDate.plusDays(6);
		return calculateCompletionPercentageByDateRange(startDate, endDate);
	}

	// 월간 완료 비율을 계산
	public double getMonthlyCompletionRate(LocalDate startDate) {
		LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
		return calculateCompletionPercentageByDateRange(startDate, endDate);
	}
}
