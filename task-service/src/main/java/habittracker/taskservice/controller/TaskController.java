package habittracker.taskservice.controller;

import habittracker.taskservice.dto.TaskDTO;
import habittracker.taskservice.exception.TaskNotFoundException;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.service.TaskService;
import habittracker.taskservice.utils.DTOtoTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Получение всех задач",
            description = "Получение списка всех задач"
    )

    @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(
            summary = "Создание новой задачи",
            description = "Создает новую задачу на основе переданных данных"
    )
    @ApiResponse(responseCode = "201", description = "Задача успешно создана")
    @PostMapping
    public Task saveTask(@RequestBody TaskDTO taskDTO) {
        Task task = DTOtoTask.convert(taskDTO);
        taskService.saveTask(task);
        return task;
    }

    @Operation(
            summary = "Получение задачи по ID",
            description = "Возвращает задачу по ее ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно найдена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }

    @Operation(
            summary = "Обновление задачи по ID",
            description = "Обновляет задачу по ее ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PutMapping("/{id}")
    public Task updateTaskById(@PathVariable int id, @RequestBody Task newTask) {
        return taskService.updateTask(id, newTask);
    }

    @Operation(
            summary = "Удаление задачи по ID",
            description = "Удаляет задачу по ее ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable int id) {
        taskService.deleteTaskById(id);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(TaskNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}