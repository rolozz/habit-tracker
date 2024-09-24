package habittracker.authservice.util.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationSuccessListenerForLog implements ApplicationListener<AuthenticationSuccessEvent> {

    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (userDetails != null) {
            if (xfHeader == null) {
                //no proxy
                log.info("Пользователь username={} успешно вошёл в систему с адреса={}",
                        userDetails.getUsername(), request.getRemoteAddr());
            } else {
                //from proxy
                log.info("Пользователь username={} успешно вошёл в систему с прокси-адреса={}",
                        userDetails.getUsername(), xfHeader.split(",")[0]);
            }
        }
    }
}