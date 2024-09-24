package habittracker.taskservice.kafka.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TaskMessage implements Serializable {

    @NotNull(message = "Task ID cannot be null")
    @NotEmpty(message = "Task ID cannot be empty")
    private String taskId;

    @NotNull(message = "Task description cannot be null")
    @Size(min = 1, max = 255, message = "Task description must be between 1 and 255 characters")
    private String taskDescription;

    @NotNull(message = "Status cannot be null")
    @NotEmpty(message = "Status cannot be empty")
    private String status;
}