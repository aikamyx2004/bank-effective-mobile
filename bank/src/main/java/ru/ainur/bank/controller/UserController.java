package ru.ainur.bank.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ainur.bank.dto.request.user.UserUpdateEmailRequest;
import ru.ainur.bank.dto.request.user.UserUpdatePhoneRequest;
import ru.ainur.bank.service.user.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "Операции над клиентами")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/change-email")
    public void changeEmail(@RequestBody @Valid UserUpdateEmailRequest request) {
        log.info("Changing email for user with request: {}", request);
        userService.changeContactInfo(u -> u.setEmail(request.getEmail()));
    }

    @PostMapping("/change-phone")
    public void changePhone(@RequestBody @Valid UserUpdatePhoneRequest request) {
        log.info("Changing phone number for user with request: {}", request);
        userService.changeContactInfo(u -> u.setPhone(request.getPhone()));
    }

    @PostMapping("/delete-email")
    public void deleteEmail() {
        log.info("Deleting email for user");
        userService.changeContactInfo(user -> user.setEmail(null));
    }

    @PostMapping("/delete-phone")
    public void deletePhone() {
        log.info("Deleting phone number for user");
        userService.changeContactInfo(user -> user.setPhone(null));
    }
}
