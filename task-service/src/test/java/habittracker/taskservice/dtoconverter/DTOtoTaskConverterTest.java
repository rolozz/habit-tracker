package habittracker.taskservice.dtoconverter;

import habittracker.taskservice.dto.TaskDTO;
import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.testutils.TestDataProvider;
import habittracker.taskservice.utils.DTOtoTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOtoTaskConverterTest {

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        taskDTO = TestDataProvider.getDTO();
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
    void testDTOtoTaskConversion() {
        Task expectedTask = task;
        Task actualTask = DTOtoTask.convert(taskDTO);
        expectedTask.setStartDate(actualTask.getStartDate());
        assertEquals(actualTask, expectedTask);
    }
}
