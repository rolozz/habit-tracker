package habittracker.taskservice.controller;

import habittracker.taskservice.exception.TaskNotFoundException;
import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.pomodoro.PomodoroService;
import habittracker.taskservice.pomodoro.PomodoroTimer;
import habittracker.taskservice.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name= "Контроллер, осуществляющий операции запуска, постановки на паузу, возобновление работы и остановку Pomodoro-таймера.")
@RequestMapping("/tasks/{id}/pomodoro")
public class PomodoroSessionController {

    private final PomodoroService pomodoroService;
    private final TaskService taskService;

    @Autowired
    public PomodoroSessionController(PomodoroService pomodoroService, TaskService taskService) {
        this.pomodoroService = pomodoroService;
        this.taskService = taskService;
    }

    @Operation(
            summary = "Запуск Pomodoro-таймера",
            description = "Запускает Pomodoro-таймер с параметрами, соответствующими задаче с указанным id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task started"),
            @ApiResponse(responseCode = "409", description = "Task is already running"),
            @ApiResponse(responseCode = "500", description = "Scheduler exception occurred")
    })
    @PostMapping("/start")
    public ResponseEntity<String> run(@PathVariable int id) throws SchedulerException {
        Task task = taskService.getTaskById(id);
        Status status = task.getStatus();
        if (status == Status.READY
                || status == Status.COMPLETE
                || status == Status.STOPPED) {
            task.setStatus(Status.STARTED);
            PomodoroTimer timer = new PomodoroTimer(
                    task.getName(),
                    task.getWorkDuration(),
                    task.getBreakDuration(),
                    task.getNCycles());
            pomodoroService.schedule(timer);
            taskService.updateTask(id, task);
            log.info("Status updated successfully. Current status: {}", task.getStatus());
            return new ResponseEntity<>("Task started", HttpStatus.OK);
        } else {
            log.warn("Requested operation doesn't match the current state: {}", status);
            return new ResponseEntity<>("Task is already running", HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Постановка Pomodoro-таймера на паузу.",
            description = "Ставит на паузу запущенный Pomodoro-таймер, соответствующий задаче с указанным id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task paused"),
            @ApiResponse(responseCode = "409", description = "Task is not running"),
            @ApiResponse(responseCode = "500", description = "Scheduler exception occurred")
    })
    @PostMapping("/pause")
    public ResponseEntity<String> pause(@PathVariable int id) throws SchedulerException {
        Task task = taskService.getTaskById(id);
        Status status = task.getStatus();
        if (status == Status.STARTED || status == Status.RESUMED) {
            task.setStatus(Status.PAUSED);
            pomodoroService.pause(task.getName());
            taskService.updateTask(id, task);
            log.info("Status updated successfully. Current status: {}", task.getStatus());
            return new ResponseEntity<>("Task paused", HttpStatus.OK);
        } else {
            log.warn("Requested operation doesn't match the current state: {}", status);
            return new ResponseEntity<>("Task is already not running", HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Возобновление работы Pomodoro-таймера",
            description = "Возобновляет работу посавленного на паузу Pomodoro-таймера, соответствующего задаче с указанным id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task resumed"),
            @ApiResponse(responseCode = "409", description = "Task is not paused"),
            @ApiResponse(responseCode = "500", description = "Scheduler exception occurred")
    })
    @PostMapping("/resume")
    public ResponseEntity<String> resume(@PathVariable int id) throws SchedulerException {
        Task task = taskService.getTaskById(id);
        Status status = task.getStatus();
        if (status == Status.PAUSED) {
            task.setStatus(Status.RESUMED);
            pomodoroService.resume(task.getName());
            taskService.updateTask(id, task);
            log.info("Status updated successfully. Current status: {}", task.getStatus());
            return new ResponseEntity<>("Task resumed", HttpStatus.OK);
        } else {
            log.warn("Requested operation doesn't match the current state: {}", status);
            return new ResponseEntity<>("Task is not paused", HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Остановка Pomodoro-таймера",
            description = "Окончательно остонавливает Pomodoro-таймер, соответствующий задаче с указанным id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task stopped"),
            @ApiResponse(responseCode = "409", description = "Task is not running"),
            @ApiResponse(responseCode = "500", description = "Scheduler exception occurred")
    })
    @PostMapping("/stop")
    public ResponseEntity<String> stop(@PathVariable int id) throws SchedulerException {
        Task task = taskService.getTaskById(id);
        Status status = task.getStatus();
        if (status != Status.STOPPED
                && status != Status.COMPLETE
                && status != Status.READY) {
            task.setStatus(Status.STOPPED);
            pomodoroService.stop(task.getName());
            taskService.updateTask(id, task);
            log.info("Status updated successfully. Current status: {}", task.getStatus());
            return new ResponseEntity<>("Task stopped", HttpStatus.OK);
        } else {
            log.warn("Requested operation doesn't match the current state: {}", status);
            return new ResponseEntity<>("Task is already not running", HttpStatus.CONFLICT);
        }
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(SchedulerException e) {
        return new ResponseEntity<>("Scheduler exception occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(TaskNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}