package habittracker.taskservice.service;

import habittracker.taskservice.exception.TaskNotFoundException;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.repository.TaskRepository;
import habittracker.taskservice.testutils.TestDataProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = TaskServiceImpl.class)
public class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskService;

    @MockBean
    private TaskRepository taskRepository;

    private static Task task1;
    private static Task task2;

    @BeforeAll
    static void setUp() {
        task1 = TestDataProvider.getTask1();
        task2 = TestDataProvider.getTask2();
    }

    @Test
    void getAllTasksTest() {

        List<Task> expectedTasks = new ArrayList<>(List.of(task1, task2));

        when(taskRepository.findAll()).thenReturn(expectedTasks);
        List<Task> actualTasks = taskService.getAllTasks();

        assertEquals(actualTasks, expectedTasks);
        assertThat(actualTasks, containsInAnyOrder(task1, task2));
        verify(taskRepository).findAll();
    }

    @Test
    void getTaskByIdSuccessTest() {
        Task expectedTask = task1;
        when(taskRepository.findById(1)).thenReturn(Optional.ofNullable(expectedTask));
        Task actualTask = taskService.getTaskById(1);
        assertEquals(1, task1.getId());
        assertEquals(actualTask, expectedTask);
        verify(taskRepository).findById(1);
    }

    @Test
    void getTaskByNameSuccessTest() {
        Task expectedTask = task1;
        when(taskRepository.findTaskByName("testName1")).thenReturn(Optional.ofNullable(expectedTask));
        Task actualTask = taskService.getTaskByName("testName1");
        assertEquals("testName1", task1.getName());
        assertEquals(actualTask, expectedTask);
        verify(taskRepository).findTaskByName("testName1");
    }

    @Test
    void updateTaskSuccessTest() {
        Task oldTask = task1;
        Task newTask = task2;
        Task expectedTask = task2;
        task2.setId(oldTask.getId());
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskRepository.save(task2)).thenReturn(expectedTask);
        Task actualTask = taskService.updateTask(1, newTask);
        assertEquals(actualTask, expectedTask);
        verify(taskRepository).existsById(1);
        verify(taskRepository).save(task2);
    }

    @Test
    void getTaskByIdFailTest() {
        when(taskRepository.findById(3)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(3), "Task with id = 3 not found");
    }

    @Test
    void getTaskByNameFailTest() {
        when(taskRepository.findTaskByName("name")).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskByName("name"), "Task with name = name not found");
    }

    @Test
    void updateTaskFailTest() {
        when(taskRepository.existsById(3)).thenReturn(false);
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(3, task1));
    }

    @Test
    void deleteTaskFailTest() {
        when(taskRepository.existsById(3)).thenReturn(false);
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(3));
    }
}
