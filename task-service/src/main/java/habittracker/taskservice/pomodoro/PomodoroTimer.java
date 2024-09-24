package habittracker.taskservice.pomodoro;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.Objects;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Класс PomodoroTimer предоставляет Job и Trigger классу PomodoroService
 * Передаёт Job'е имя задачи;
 * В конструкторе создаёт Pomodoro и помещает в Job в качестве значения DataMap с ключом "pomodata";
 * Передаёт Trigger'у общее количество посекундных срабатываний,
 * исходя из параметров Pomodoro (в .withRepeatCount(...)).
 */

public class PomodoroTimer {

    private final String name;
    private final int workDuration;
    private final int breakDuration;
    private final int nCycles;
    private final JobDataMap dataMap = new JobDataMap();

    public PomodoroTimer(String name, int workDuration, int breakDuration, int nCycles) {
        this.name = name;
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
        this.nCycles = nCycles;
        Pomodoro tomato = new Pomodoro(workDuration, breakDuration, nCycles);
        this.dataMap.put("pomodata", tomato);
    }

    /**
     *Job для scheduler'а.
     * @return Собственно Job.
     */
    public JobDetail getJob() throws SchedulerException {
        JobDetail job = newJob(PomodoroJob.class)
                .withIdentity(name)
                .usingJobData(dataMap)
                .build();
        return job;
    }

    /**
     *Trigger для scheduler'а.
     * @return Собственно Trigger.
     */
    public Trigger getTrigger() {
        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(1)
                        .withRepeatCount(nCycles * workDuration + (nCycles - 1) * breakDuration))
                .build();
        return trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PomodoroTimer that = (PomodoroTimer) o;
        return workDuration == that.workDuration && breakDuration == that.breakDuration && nCycles == that.nCycles && Objects.equals(name, that.name);
    }
}
