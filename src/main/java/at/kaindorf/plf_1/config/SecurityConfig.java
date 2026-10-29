package at.kaindorf.plf_1.config;

import at.kaindorf.plf_1.entities.MyUser;
import at.kaindorf.plf_1.repositories.MyUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MyUserRepository userRepository) {

        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                log.info("LOG --> loadUserByUsername" + username);

                MyUser user = userRepository.findByUsername(username);

                if (user == null) {
                    throw new UsernameNotFoundException(username + "LOG-ERROR --> USER NOT FOUND !!!");
                }

                UserDetails userDetails = User.builder()
                        .username(username)
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build();

                return userDetails;
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable()) // um user zu adden
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/**").hasRole("BENUTZER") // benutzer darf NUR GET methoden schicken
                        .requestMatchers(HttpMethod.POST, "/api/v1/adduser").hasRole("ADMIN") // admin darf KEINE GET methoden schicken NUR POST
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


}
