package com.clush.todolist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
