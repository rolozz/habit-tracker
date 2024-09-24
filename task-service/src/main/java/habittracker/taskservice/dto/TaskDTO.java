package habittracker.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String name;
    private String description;
    private int workDuration;
    private int breakDuration;
    private int nCycles;
    private Long userId;
}
