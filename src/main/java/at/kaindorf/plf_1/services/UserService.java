package at.kaindorf.plf_1.services;

import at.kaindorf.plf_1.DTO.AddUserRequest;
import at.kaindorf.plf_1.entities.MyUser;
import at.kaindorf.plf_1.entities.ROLES;
import at.kaindorf.plf_1.repositories.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addUser(AddUserRequest request) {
        if (request == null
                || request.username() == null
                || request.username().isBlank()
                || request.password() == null
                || request.password().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Benutzername und Passwort dürfen nicht leer sein");
        }

        String username = request.username().trim();

        if (myUserRepository.findByUsername(username) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists");
        }

        MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(ROLES.BENUTZER);

        myUserRepository.save(user);
    }
}