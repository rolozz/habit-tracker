package habittracker.taskservice.service;

import habittracker.taskservice.model.task.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task saveTask(Task task);
    Task getTaskById(int id);
    Task getTaskByName(String name);
    Task updateTask(int id, Task task);
    void deleteTaskById(int id);
}
