package at.kaindorf.plf_1.controllers;

import at.kaindorf.plf_1.DTO.AddUserRequest;
import at.kaindorf.plf_1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/adduser")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody AddUserRequest request) {

        try {
            userService.addUser(request);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }

    }

}
