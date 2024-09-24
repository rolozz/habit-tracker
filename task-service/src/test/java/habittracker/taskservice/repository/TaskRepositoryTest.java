package habittracker.taskservice.repository;

import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.testutils.TestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Task task;

    @BeforeEach
    void setUp() {
        task = TestDataProvider.getTask1();
    }

    @Test
    void findAllTasksTest() {
        Task expectedTask = testEntityManager.find(Task.class, 1);
        testEntityManager.clear();
        List<Task> actualTask = taskRepository.findAll();
        assertThat(actualTask).hasSize(1).containsExactlyInAnyOrder(expectedTask);
    }

    @Test
    void saveTaskTest() {
        Task expectedTask = task;
        Task actualTask = taskRepository.save(expectedTask);
        assertThat(actualTask).isEqualTo(expectedTask);
    }

    @Test
    void findTaskByNameTest() {
        Task expectedTask = testEntityManager.find(Task.class, 1);
        Task actualTask = taskRepository.findTaskByName(expectedTask.getName()).orElse(new Task());
        assertThat(actualTask).isEqualTo(expectedTask);
    }
}
