package habittracker.taskservice.model.task;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
@EqualsAndHashCode

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private Status status;

    private LocalDateTime startDate;
    private LocalDateTime stopDate;

    private int workDuration;
    private int breakDuration;
    private int nCycles;
    private Long userId;
}
