package habittracker.authservice.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomInfoActuator implements InfoContributor {

    private final Environment environment;
    private final BuildProperties buildProperties;
    private final MetricsEndpoint metricsEndpoint;
    private final GitProperties gitProperties;

    public CustomInfoActuator(Environment environment, BuildProperties buildProperties,
                              MetricsEndpoint metricsEndpoint, GitProperties gitProperties) {
        this.environment = environment;
        this.buildProperties = buildProperties;
        this.metricsEndpoint = metricsEndpoint;
        this.gitProperties = gitProperties;
    }

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> mainInfo = new HashMap<>();
        mainInfo.put("application_name", buildProperties.getName());
        mainInfo.put("artifact_id", buildProperties.getArtifact());
        mainInfo.put("launch_time", buildProperties.getTime());
        mainInfo.put("app_version", buildProperties.getVersion());
        mainInfo.put("context_path", environment.getProperty("server.servlet.context-path", "/"));
        mainInfo.put("active_profiles", environment.getActiveProfiles());
        builder.withDetail("main_info", mainInfo);
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("uptime", metricsEndpoint.metric("process.uptime", null));
        additionalInfo.put("spu_usage", metricsEndpoint.metric("process.cpu.usage", null));
        additionalInfo.put("memory_usage", metricsEndpoint.metric("jvm.memory.used", null));
//        additionalInfo.put("build version", "??????");               // на будущее...
        additionalInfo.put("current_branch", gitProperties.getBranch());
        additionalInfo.put("last_commit", gitProperties.getCommitId());
        builder.withDetail("additional_info", additionalInfo);
    }
}
