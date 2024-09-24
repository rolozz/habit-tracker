package habittracker.taskservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import habittracker.taskservice.exception.TaskNotFoundException;
import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.service.TaskServiceImpl;
import habittracker.taskservice.testutils.TestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    private Task task;

    private List<Task> expectedTasks;

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
        expectedTasks = List.of(task);
    }

    @Test
    void getAllTasksTest() throws Exception {
        when(taskService.getAllTasks()).thenReturn(expectedTasks);
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTasks)));
    }

    @Test
    void getTaskByIdTest() throws Exception {
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(get("/tasks/0"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(task)));
    }

    @Test
    void getTaskByIdFailTest() throws Exception {
        when(taskService.getTaskById(1)).thenThrow(new TaskNotFoundException(1));
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with id = 1 not found"));
    }

    @Test
    void getTaskByNameTest() throws Exception {
        when(taskService.getTaskById(0)).thenReturn(task);
        mockMvc.perform(get("/tasks/0"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(task)));
    }

    @Test
    void getTaskByNameFailTest() throws Exception {
        when(taskService.getTaskById(1)).thenThrow(new TaskNotFoundException(1));
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with id = 1 not found"));
    }

    @Test
    void updateTaskIdTest() throws Exception {
        Task newTask = TestDataProvider.getTask2();
        when(taskService.getTaskById(0)).thenReturn(task);
        when(taskService.updateTask(0, newTask)).thenReturn(newTask);
        mockMvc.perform(put("/tasks/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isOk())
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult result) throws Exception {
                        Task responseTask = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);
                        task.setStartDate(responseTask.getStartDate());
                        task.setId(0);
                    }
                })
                .andExpect(content().json(objectMapper.writeValueAsString(newTask)));
    }

    @Test
    void saveTaskTest() throws Exception {
        when(taskService.saveTask(task)).thenReturn(task);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult result) throws Exception {
                        Task responseTask = objectMapper.readValue(result.getResponse().getContentAsString(), Task.class);
                        task.setStartDate(responseTask.getStartDate());
                    }
                })
                .andExpect(content().json(objectMapper.writeValueAsString(task)));
    }

    @Test
    void deleteTaskByIdTest() throws Exception {
        mockMvc.perform(delete("/tasks/0"))
                .andExpect(status().isOk());
        verify(taskService).deleteTaskById(0);
    }
}
