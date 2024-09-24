package habittracker.taskservice.exception;

public class TaskNotFoundException extends RuntimeException {
    private final String message;
    public TaskNotFoundException(int id) {
        this.message = "Task with id = " + id + " not found";
    }

    public TaskNotFoundException(String name) {
        this.message = "Task with name = " + name + " not found";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
