package com.example.calendar.Controller;

import com.example.calendar.DTO.TodoDTO;
import com.example.calendar.DTO.TodoOutDto;
import com.example.calendar.Entity.Todo;
import com.example.calendar.Service.TodosService;
import com.example.calendar.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodosController {

    private final TodosService todosService;
    private final UserService userService;

    @Autowired
    public TodosController(TodosService todosService, UserService userService) {
        this.todosService = todosService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Todo> saveTodo(@RequestBody TodoDTO todoDto) {
        Long userId = userService.findLoggedInUserId();
        Todo savedTodo = todosService.saveTodo(todoDto, userId);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getTodos() {
        Long userId = userService.findLoggedInUserId();
        List<Todo> todos = todosService.getTodosByUserId(userId);
        List<TodoOutDto> todosOut = new ArrayList<>();
        for(int i = 0; i < todos.size(); i++){
            TodoOutDto t = new TodoOutDto();
            t.setTodoStatus(todos.get(i).getTodoStatus().toString());
            t.setTitle(todos.get(i).getTitle());
            //t.getEndTime(todos.get(i).getEndTime())
        }
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable("id") Long id) {
        String message = todosService.deleteTodo(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") long id, @RequestBody TodoDTO todoDto) {
        Todo updatedTodo = todosService.updateTodo(id, todoDto);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }
}
