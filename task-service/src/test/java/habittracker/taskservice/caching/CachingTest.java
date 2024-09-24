package habittracker.taskservice.caching;

import habittracker.taskservice.TaskServiceApplication;
import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.repository.TaskRepository;
import habittracker.taskservice.service.TaskServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = TaskServiceApplication.class)
public class CachingTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    private TaskServiceImpl taskService;

    @MockBean
    private TaskRepository taskRepository;

    private static Task task1;

    @BeforeEach
    @CacheEvict("task")
    public void setUp() {
        task1 = new Task();
        task1.setId(1);
        task1.setDescription("testDescription1");
        task1.setName("testName1");
        task1.setStatus(Status.STARTED);
        task1.setWorkDuration(20);
        task1.setBreakDuration(5);
        task1.setNCycles(2);
        task1.setStartDate(LocalDateTime.parse("2024-10-13T00:00:00.000"));
        task1.setStopDate(LocalDateTime.parse("2024-10-13T00:00:00.000"));
        task1.setUserId(1L);
    }

    @Test
    void getTaskByIdCacheTest() {
        when(taskRepository.findById(1)).thenReturn(Optional.ofNullable(task1));
        Task testedTask = taskService.getTaskById(1);
        Task cachedTask = Objects.requireNonNull(cacheManager.getCache("task")).get(1, Task.class);
        assertEquals(testedTask, cachedTask);
        verify(taskRepository).findById(1);
    }

    @AfterEach
    @CacheEvict("task")
    public void clear() {

    }
}
