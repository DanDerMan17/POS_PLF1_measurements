package at.kaindorf.plf_1.controllers;

import at.kaindorf.plf_1.DTO.AddUserRequest;
import at.kaindorf.plf_1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/adduser")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody AddUserRequest request) {

        userService.addUser(request);

    }

}
