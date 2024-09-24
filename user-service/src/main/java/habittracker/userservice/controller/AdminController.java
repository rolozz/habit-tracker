package habittracker.userservice.controller;

import habittracker.userservice.audit.UserActionLog;
import habittracker.userservice.model.Role;
import habittracker.userservice.model.User;
import habittracker.userservice.repository.RoleRepository;
import habittracker.userservice.repository.UserActionLogRepository;
import habittracker.userservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserActionLogRepository logRepository;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, UserActionLogRepository logRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.logRepository = logRepository;
    }

    @PostMapping("/assign-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignRoleToUser(@RequestParam String email, @RequestParam String roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok("Role assigned successfully");
    }

    // Метод для удаления пользователя (только для админов)
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Метод для блокировки пользователей (доступен только для админов)
    @PostMapping("/block-user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Логика блокировки пользователя (например, установка флага isActive = false)
        // user.setActive(false);
        userRepository.save(user);

        return ResponseEntity.ok("User blocked successfully");
    }

    // Разблокировка пользователя (доступен только для админов)
    @PostMapping("/unblock-user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> unblockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Логика разблокировки пользователя (например, установка флага isActive = true)
        // user.setActive(true);
        userRepository.save(user);

        return ResponseEntity.ok("User unblocked successfully");
    }

    @GetMapping("/admin/logs")
    public List<UserActionLog> getLogs() {
        return logRepository.findAll();
    }
}