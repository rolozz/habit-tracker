package habittracker.taskservice.pomodoroservice;

import habittracker.taskservice.pomodoro.PomodoroService;
import habittracker.taskservice.pomodoro.PomodoroTimer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;


@SpringBootTest(classes = PomodoroService.class)
public class PomodoroServiceTest {

    @Autowired
    private PomodoroService pomodoroService;

    @MockBean
    private Scheduler scheduler;

    @Test
    void scheduleTimerTest() throws SchedulerException {
        PomodoroTimer pomodoroTimer = new PomodoroTimer("test", 20, 5, 1);
        pomodoroService.schedule(new PomodoroTimer("test", 20, 5, 1));
        verify(scheduler).scheduleJob(pomodoroTimer.getJob(), pomodoroTimer.getTrigger());
    }

    @Test
    void pauseTimerTest() throws SchedulerException {
        pomodoroService.pause("test");
        verify(scheduler).pauseJob(new JobKey("test"));
    }

    @Test
    void resumeTimerTest() throws SchedulerException {
        pomodoroService.resume("test");
        verify(scheduler).resumeJob(new JobKey("test"));
    }

    @Test
    void stopTimerTest() throws SchedulerException {
        pomodoroService.stop("test");
        verify(scheduler).deleteJob(new JobKey("test"));
    }

    @AfterEach
    void stopScheduler() throws SchedulerException {
        scheduler.shutdown();
    }
}
