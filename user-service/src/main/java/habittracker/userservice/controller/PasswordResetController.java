package habittracker.userservice.controller;

import habittracker.userservice.model.PasswordResetToken;
import habittracker.userservice.model.User;
import habittracker.userservice.repository.PasswordResetTokenRepository;
import habittracker.userservice.repository.UserRepository;
import habittracker.userservice.service.EmailService;
import habittracker.userservice.service.UserActionLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetController.class);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserActionLogService actionLogService;

    // Внедрение всех зависимостей через конструктор
    public PasswordResetController(UserRepository userRepository,
                                   PasswordResetTokenRepository tokenRepository,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder, UserActionLogService actionLogService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.actionLogService = actionLogService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email doesn't exist"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetUrl);

        // Логирование запроса на сброс пароля
        LOGGER.info("Password reset link sent to {}", email);

        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            LOGGER.warn("Attempt to reset password with expired token");
            return ResponseEntity.badRequest().body("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Логирование успешного сброса пароля
        LOGGER.info("Password reset successfully for user {}", user.getEmail());

        // Логирование сброса пароля
        actionLogService.logAction(user.getUsername(), "Password reset");

        return ResponseEntity.ok("Password reset successfully.");
    }
}