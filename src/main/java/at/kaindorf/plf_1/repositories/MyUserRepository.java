package at.kaindorf.plf_1.repositories;

import at.kaindorf.plf_1.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByUsername(String username);

}
