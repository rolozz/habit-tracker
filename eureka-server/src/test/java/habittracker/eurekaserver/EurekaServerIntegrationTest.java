package habittracker.eurekaserver;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EurekaIntegrationTest {

    @LocalServerPort
    private int port;

    private final WebClient webClient = WebClient.create();

    @Test
    void testServiceRegistration() {
        String eurekaUrl = "http://localhost:" + port + "/eureka/apps";
        Mono<String> responseMono = webClient.get()
                .uri(eurekaUrl)
                .retrieve()
                .bodyToMono(String.class);

        String response = responseMono.block();
        assertThat(response).contains("<applications>");
    }

    @Test
    void testServiceDeregistration() throws InterruptedException {
        Thread.sleep(40001);

        String eurekaUrl = "http://localhost:" + port + "/eureka/apps";
        Mono<String> responseMono = webClient.get()
                .uri(eurekaUrl)
                .retrieve()
                .bodyToMono(String.class);

        String response = responseMono.block();
        assertThat(response).doesNotContain("api-gateway");
    }
}


