package at.kaindorf.plf_1.services;

import at.kaindorf.plf_1.entities.MyUser;
import at.kaindorf.plf_1.repositories.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static at.kaindorf.plf_1.entities.ROLES.ADMIN;

@Service
@RequiredArgsConstructor
public class InitUsers implements ApplicationRunner {

    private final MyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (userRepository.findByUsername("admin") == null) {

            MyUser admin = new MyUser();

            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(ADMIN);

            userRepository.save(admin);
        }
    }
}
