package habittracker.taskservice.utils;

import habittracker.taskservice.dto.TaskDTO;
import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;

import java.time.LocalDateTime;

public class DTOtoTask {
    public static Task convert(TaskDTO taskDTO) {
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(Status.READY);
        task.setWorkDuration(taskDTO.getWorkDuration());
        task.setBreakDuration(taskDTO.getBreakDuration());
        task.setNCycles(taskDTO.getNCycles());
        task.setStartDate(LocalDateTime.now());
        task.setUserId(taskDTO.getUserId());
        return task;
    }
}
