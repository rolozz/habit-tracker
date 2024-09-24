package habittracker.taskservice.pomodoro;

import lombok.Getter;
import java.io.Serializable;

/**
 * Класс Pomodoro реализует логику техники помидора.
 * Он принимает время работы и время перерыва (пока тупо в секундах),
 * а также количество циклов работы (пока упрощённая стратегия)
 * определяется в PomodoroTimer и помещается в Job
 * в качестве значения DataMap c ключом "data".
 */

public class Pomodoro implements Serializable {
    @Getter
    private long count = 0;
    private int cycleCount = 0;
    private int workDuration = 25;
    private int breakDuration = 5;
    private final int nCycles;
    @Getter
    private Stage stage = Stage.WORK;

    public Pomodoro(int workDuration, int breakDuration, int nCycles) {
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
        this.nCycles = nCycles;
    }

    /**
     *Обновляет посекундный счётчик.
     *При достижении лимита для текущей стадии (работа/перерыв)
     *обнуляет счётчик и переводит стадию в следующую.
     */
    public void increase() {
        if (stage == Stage.WORK && count >= workDuration) {
            stage = Stage.BREAK;
            System.out.println("Time to chill");//TEMP!!
            count = 0;
            cycleCount++;
        }
        if (cycleCount >= nCycles) {
            System.out.println("T'is done");//TEMP!!
            stage = Stage.DONE;
        }
        if (cycleCount < nCycles && stage == Stage.BREAK && count >= breakDuration) {
            stage = Stage.WORK;
            System.out.println("Time to work");//TEMP!!
            count = 0;
        }
        System.out.println(count++);
    }

}
