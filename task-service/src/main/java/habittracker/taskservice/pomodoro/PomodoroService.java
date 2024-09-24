package habittracker.taskservice.pomodoro;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Класс PomodoroService реалзует операций с помидоро-таймером:
 * запуск задачи, паузу, возобновление и остановку.
 */

@Service
public class PomodoroService {

    private final Scheduler scheduler;

    @Autowired
    public PomodoroService(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(PomodoroTimer timer) throws SchedulerException {
        scheduler.scheduleJob(timer.getJob(), timer.getTrigger());
    }

    public void pause(String name) throws SchedulerException {
        scheduler.pauseJob(new JobKey(name));
    }

    public void resume(String name) throws SchedulerException {
        scheduler.resumeJob(new JobKey(name));
    }

    public void stop(String name) throws SchedulerException {
        scheduler.deleteJob(new JobKey(name));
    }
}
