package habittracker.authservice.util.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class LogoutListenerForLog implements ApplicationListener<SessionDestroyedEvent> {

    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        List<SecurityContext> listSecurityContext = event.getSecurityContexts();
        final String xfHeader = request.getHeader("X-Forwarded-For");
        UserDetails userDetails;
        for (SecurityContext securityContext : listSecurityContext) {
            userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
            if (userDetails != null) {
                if (xfHeader == null) {
                    //no proxy
                    log.info("Пользователь username={} успешно вышел из системы с адреса={}",
                            userDetails.getUsername(), request.getRemoteAddr());
                } else {
                    //from proxy
                    log.info("Пользователь username={} успешно вышел из системы с прокси-адреса={}",
                            userDetails.getUsername(), xfHeader.split(",")[0]);
                }
            }
        }
    }
}
