package habittracker.authservice.util.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationFailureListenerForLog
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            //no proxy
            log.error("Попытка зайти в систему пользователя username={} была не успешной с адреса={}",
                    event.getAuthentication().getName(), request.getRemoteAddr());
        } else {
            //from proxy
            log.error("Попытка зайти в систему пользователя username={} была не успешной с прокси-адреса={}",
                    event.getAuthentication().getName(), xfHeader.split(",")[0]);
        }
    }
}
