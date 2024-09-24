package habittracker.taskservice.pomodoro;

import habittracker.taskservice.model.task.Status;
import habittracker.taskservice.model.task.Task;
import habittracker.taskservice.service.TaskService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Класс PomodoroJob реализует метод execute() для Job:
 * обновляет посекундный счётчик;
 * исходя из его значения уведомляет о состоянии Pomodoro.
 */

@Component
public class PomodoroJob implements Job {

    KafkaTemplate<String, Object> kafkaTemplate;
    TaskService taskService;

    @Autowired
    public PomodoroJob(KafkaTemplate<String, Object> kafkaTemplate, TaskService taskService) {
        this.kafkaTemplate = kafkaTemplate;
        this.taskService = taskService;
    }

    /**
     *Извлекает из контекста DataMap и по ключу "pomodata" получает Pomodoro.
     *Обновляет счётчик Pomodoro (.increase()) и делает то, что нужно,
     *исходя из его значения:
     *По завершении циклов работы/перерыва обновляет состояние Pomodoro
     *и отправляет соответствующее сообщение в топик "pomodoro";
     *По завершении задачи обновляет статус задачи в БД.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Pomodoro tomato = (Pomodoro) dataMap.get("pomodata");
        tomato.increase();
        if (tomato.getCount() == 1) {
            Stage stage = tomato.getStage();
            kafkaTemplate.send("pomodoro", stage);
            if (stage == Stage.DONE) {
                String name = context.getJobDetail().getKey().getName();
                Task task = taskService.getTaskByName(name);
                task.setStatus(Status.COMPLETE);
                task.setStopDate(LocalDateTime.now());
                taskService.saveTask(task);
            }
            System.out.println();
        }
    }
}
