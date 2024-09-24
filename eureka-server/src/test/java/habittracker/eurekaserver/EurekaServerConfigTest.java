package habittracker.eurekaserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class EurekaServerConfigTest {

    ApplicationContext applicationContext;

    @Autowired
    public EurekaServerConfigTest(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(applicationContext).isNotNull();
    }

    @Test
    void eurekaServerConfig() {
        EurekaServerConfigBean eurekaServerConfigBean  = applicationContext.getBean(EurekaServerConfigBean.class);
        Assertions.assertThat(eurekaServerConfigBean).isNotNull();
    }
}
