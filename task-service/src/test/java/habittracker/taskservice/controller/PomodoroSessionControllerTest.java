package habittracker.taskservice.controller;

import habittracker.taskservice.exception.TaskNotFoundException;
import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.pomodoro.PomodoroService;
import habittracker.taskservice.pomodoro.PomodoroTimer;
import habittracker.taskservice.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PomodoroSessionController.class)
public class PomodoroSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    @MockBean
    private PomodoroService pomodoroService;

    Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setName("testName1");
        task.setDescription("testDescription1");
        task.setStatus(Status.READY);
        task.setWorkDuration(20);
        task.setBreakDuration(5);
        task.setNCycles(2);
        task.setUserId(1L);
    }

    @Test
    void startTimerSuccessTest() throws Exception {
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/start"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task started"));
        verify(taskService).getTaskById(0);
        task.setStatus(Status.STARTED);
        verify(taskService).updateTask(0, task);
        verify(pomodoroService).schedule(new PomodoroTimer("testName1", 20, 5, 2));
    }

    @Test
    void startTimerWithNonExistentIdTest() throws Exception {
        when(taskService.getTaskById(1)).thenThrow(new TaskNotFoundException(1));
        mockMvc.perform(post("/tasks/1/pomodoro/start"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with id = 1 not found"));
        verify(taskService).getTaskById(1);
    }

    @ParameterizedTest
    @CsvSource({"STARTED", "PAUSED", "RESUMED"})
    void startTimerAlreadyRunningTest(Status status) throws Exception {
        task.setStatus(status);
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/start"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Task is already running"));
        verify(taskService).getTaskById(0);
    }

    @ParameterizedTest
    @CsvSource({"STARTED", "RESUMED"})
    void pauseTimerSuccessTest(Status status) throws Exception {
        task.setStatus(status);
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/pause"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task paused"));
        verify(taskService).getTaskById(0);
        task.setStatus(Status.PAUSED);
        verify(taskService).updateTask(0, task);
        verify(pomodoroService).pause("testName1");
    }

    @ParameterizedTest
    @CsvSource({"READY", "PAUSED", "STOPPED"})
    void pauseTimerNotRunningTest(Status status) throws Exception {
        task.setStatus(status);
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/pause"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Task is already not running"));
        verify(taskService).getTaskById(0);
    }

    @Test
    void schedulerExceptionHandlingTest() throws Exception {
        task.setStatus(Status.STARTED);
        task.setName("incorrectName");
        when(taskService.getTaskById(0)).thenReturn(task);
        doThrow(new SchedulerException()).when(pomodoroService).pause("incorrectName");
        mockMvc.perform(post("/tasks/0/pomodoro/pause"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Scheduler exception occurred"));
    }

    @Test
    void resumeTimerSuccessTest() throws Exception {
        task.setStatus(Status.PAUSED);
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/resume"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task resumed"));
        verify(taskService).getTaskById(0);
        task.setStatus(Status.RESUMED);
        verify(taskService).updateTask(0, task);
        verify(pomodoroService).resume("testName1");
    }

    @ParameterizedTest
    @CsvSource({"READY", "STARTED", "RESUMED"})
    void resumeTimerNotPausedTest(Status status) throws Exception {
        task.setStatus(status);
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/resume"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Task is not paused"));
        verify(taskService).getTaskById(0);
    }

    @ParameterizedTest
    @CsvSource({"STARTED", "PAUSED", "RESUMED"})
    void stopTimerSuccessTest(Status status) throws Exception {
        task.setStatus(status);
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/stop"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task stopped"));
        verify(taskService).getTaskById(0);
        task.setStatus(Status.STOPPED);
        verify(taskService).updateTask(0, task);
        verify(pomodoroService).stop("testName1");
    }

    @ParameterizedTest
    @CsvSource({"READY", "STOPPED"})
    void stopTimerNotRunningTest(Status status) throws Exception {
        task.setStatus(status);
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(post("/tasks/0/pomodoro/stop"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Task is already not running"));
        verify(taskService).getTaskById(0);
    }
}
