package com.clush.todolist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clush.todolist.entity.ToDo;
import com.clush.todolist.repository.ToDoRepository;

@Service
public class ToDoService {

	@Autowired
	private ToDoRepository todoRepository;
	
	public void addWork (ToDo todo) {
		todo.setCompleted(false);
		
		todoRepository.save(todo);
	}
	
	public List<ToDo> getWorkList(){
		
		List<ToDo> data = new ArrayList<>();
		data = todoRepository.findAll();
		
		return data;
	}
	
	public ToDo editWork (Long id, ToDo todo) {
		Optional<ToDo> readyToDo = todoRepository.findById(id);
		
		ToDo updateWork = readyToDo.get();
		updateWork.setTitle(todo.getTitle());
		updateWork.setDescription(todo.getDescription());
		updateWork.setCompleted(todo.isCompleted());
		return todoRepository.save(updateWork);
	}
	
	public boolean deleteWork(Long id) {
		
		todoRepository.deleteById(id);
		return true;
	}
	
}
