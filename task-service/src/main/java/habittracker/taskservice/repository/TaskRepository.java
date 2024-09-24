package habittracker.taskservice.repository;

import habittracker.taskservice.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findTaskByName(String name);
}
