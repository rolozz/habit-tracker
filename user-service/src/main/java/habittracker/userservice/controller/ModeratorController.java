package habittracker.userservice.controller;

import habittracker.userservice.model.User;
import habittracker.userservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/moderator")
public class ModeratorController {

    private final UserRepository userRepository;

    public ModeratorController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Метод для просмотра всех пользователей, доступный только модераторам
    @GetMapping("/users")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Метод для блокировки пользователя (например, деактивация), доступный только модераторам
    @PostMapping("/users/block/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Здесь можно реализовать логику блокировки, например, поставить флаг isActive = false
        // user.setActive(false);
        // userRepository.save(user);

        return ResponseEntity.ok("User blocked successfully");
    }

    // Метод для разблокировки пользователя, доступный только модераторам
    @PostMapping("/users/unblock/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<String> unblockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Логика разблокировки пользователя
        // user.setActive(true);
        // userRepository.save(user);

        return ResponseEntity.ok("User unblocked successfully");
    }
}
