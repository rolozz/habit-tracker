package habittracker.taskservice.loadtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import habittracker.taskservice.testutils.TestDataProvider;
import org.apache.http.entity.ContentType;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

@SpringBootTest
public class LoadTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void taskControllerGetAllPerformanceTest() throws IOException {
        TestPlanStats stats = testPlan(
                threadGroup(2, 10,
                        httpSampler("http://localhost:8080/tasks")
                )
        ).run();
        assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
    }

    @Test
    public void taskControllerPostPerformanceTest() throws IOException {
        TestPlanStats stats = testPlan(
                threadGroup(2, 10,
                        httpSampler("http://localhost:8080/tasks")
                                .post(objectMapper.writeValueAsString(TestDataProvider.getDTO()), ContentType.APPLICATION_JSON)
                )
        ).run();
        assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
    }

    @Test
    public void pomodoroSessionControllerPerformanceTest() throws IOException {
        TestPlanStats stats = testPlan(
                threadGroup(1, 3,
                        httpSampler("http://localhost:8080/tasks/1/pomodoro/start")
                                .method(HTTPConstants.POST)
                ),
                threadGroup(1, 3,
                        httpSampler("http://localhost:8080/tasks/1/pomodoro/pause")
                                .method(HTTPConstants.POST)
                ),
                threadGroup(1, 3,
                        httpSampler("http://localhost:8080/tasks/1/pomodoro/resume")
                                .method(HTTPConstants.POST)
                ),
                threadGroup(1, 3,
                        httpSampler("http://localhost:8080/tasks/1/pomodoro/stop")
                                .method(HTTPConstants.POST)
                )
        ).run();
        assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
    }

}