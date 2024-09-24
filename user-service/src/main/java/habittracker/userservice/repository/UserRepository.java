package habittracker.userservice.repository;

import habittracker.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Поиск пользователя по email
    Optional<User> findByEmail(String email);
}