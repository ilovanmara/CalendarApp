package com.example.calendar.Service;

import com.example.calendar.DTO.TodoDTO;
import com.example.calendar.Entity.Todo;
import com.example.calendar.Entity.TodoStatus;
import com.example.calendar.Entity.User;
import com.example.calendar.Repository.TodosRepository;
import com.example.calendar.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TodosService {
    @Autowired
    private final TodosRepository todosRepository;
    @Autowired
    private final UserRepository userRepository;

    public TodosService(TodosRepository todosRepository, UserRepository userRepository) {
        this.todosRepository = todosRepository;
        this.userRepository = userRepository;
    }

    public Todo saveTodo(TodoDTO todoDto, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Todo todo = new Todo();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(todoDto.getStartTime(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(todoDto.getEndTime(), formatter);
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setStartTime(startDate);
        todo.setEndTime(endDate);
        todo.setTodoStatus(TodoStatus.TODO);
        todo.setUser(user);
        user.getTodos().add(todo);
        return todosRepository.save(todo);
    }
    public List<Todo> getTodos(){
        return todosRepository.findAll();
    }

    public List<Todo> getTodosByUserId(Long userId){
        return todosRepository.findByUserId(userId);
    }

    public String deleteTodo(Long id){
        todosRepository.deleteById(id);
        return "todo remove";
    }

    public Todo updateTodo(long id, TodoDTO todoDto){
        Todo todo = todosRepository.findById(id).orElse(null);
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(todoDto.getStartTime(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(todoDto.getEndTime(), formatter);
        todo.setStartTime(startDate);
        todo.setEndTime(endDate);
        TodoStatus status = TodoStatus.valueOf(todoDto.getTodoStatus());
        todo.setTodoStatus(status);
        return todosRepository.save(todo);
    }
}
