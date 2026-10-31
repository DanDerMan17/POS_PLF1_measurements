package at.kaindorf.plf_1.services;

import at.kaindorf.plf_1.DTO.AddUserRequest;
import at.kaindorf.plf_1.entities.MyUser;
import at.kaindorf.plf_1.entities.ROLES;
import at.kaindorf.plf_1.repositories.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(AddUserRequest request) {
        if (request == null
                || request.username() == null
                || request.username().isBlank()
                || request.password() == null
                || request.password().isBlank()) {
            throw new IllegalArgumentException(
                    "Benutzername und Passwort dürfen nicht leer sein");
        }

        String username = request.username().trim();

        if (myUserRepository.findByUsername(username) != null) {
            throw new IllegalStateException("Username already exists");
        }

        MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(ROLES.BENUTZER);

        myUserRepository.save(user);
    }
}
