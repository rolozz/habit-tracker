package habittracker.taskservice.testutils;

import habittracker.taskservice.dto.TaskDTO;
import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;
import java.time.LocalDateTime;

public class TestDataProvider {

    public static Task getTask1() {
        Task task = new Task();
        task.setId(1);
        task.setDescription("testDescription1");
        task.setName("testName1");
        task.setStatus(Status.STARTED);
        task.setWorkDuration(20);
        task.setBreakDuration(5);
        task.setNCycles(2);
        task.setStartDate(LocalDateTime.parse("2024-10-13T00:00:00.000"));
        task.setStopDate(LocalDateTime.parse("2024-10-13T00:00:00.000"));
        task.setUserId(1L);
        return task;
    }

    public static Task getTask2() {
        Task task = new Task();
        task.setId(2);
        task.setDescription("testDescription2");
        task.setName("testName2");
        task.setStatus(Status.STOPPED);
        task.setWorkDuration(20);
        task.setBreakDuration(5);
        task.setNCycles(3);
        task.setStartDate(LocalDateTime.parse("2024-10-13T00:00:00.000"));
        task.setStopDate(LocalDateTime.parse("2024-10-13T00:00:00.000"));
        task.setUserId(2L);
        return task;
    }

    public static TaskDTO getDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("testName1");
        taskDTO.setDescription("testDescription1");
        taskDTO.setWorkDuration(20);
        taskDTO.setBreakDuration(5);
        taskDTO.setNCycles(2);
        taskDTO.setUserId(1L);
        return taskDTO;
    }
}
