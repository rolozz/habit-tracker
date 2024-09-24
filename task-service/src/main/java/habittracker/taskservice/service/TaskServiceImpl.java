package habittracker.taskservice.service;

import habittracker.taskservice.exception.TaskNotFoundException;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    @Override
    public Task saveTask(Task task) {
        taskRepository.save(task);
        return task;
    }

    @Override
    @Cacheable("task")
    public Task getTaskById(int id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public Task getTaskByName(String name) {
        return taskRepository.findTaskByName(name).orElseThrow(() -> new TaskNotFoundException(name));
    }

    @Transactional
    @Override
    public Task updateTask(int id, Task newTask) {
        if (!taskRepository.existsById(id)) throw new TaskNotFoundException(1);
        newTask.setId(id);
        taskRepository.save(newTask);
        return newTask;
    }

    @Transactional
    @Override
    @CacheEvict("task")
    public void deleteTaskById(int id) {
        if (!taskRepository.existsById(id)) throw new TaskNotFoundException(1);
        taskRepository.deleteById(id);
    }

}
