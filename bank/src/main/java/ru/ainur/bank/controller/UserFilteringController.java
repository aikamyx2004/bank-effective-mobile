package ru.ainur.bank.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ainur.bank.dto.request.user.UserSearchRequest;
import ru.ainur.bank.dto.response.UserSearchResponse;
import ru.ainur.bank.service.user.UserService;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/filter")
@Tag(name = "Фильтрация пользователей")
@Slf4j
public class UserFilteringController {
    private final UserService userService;

    @GetMapping("/search")
    public UserSearchResponse searchClients(@RequestParam(required = false) LocalDate dateOfBirth,
                                            @RequestParam(required = false) String phone,
                                            @RequestParam(required = false) String fullName,
                                            @RequestParam(required = false) String email,
                                            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("Received user search request with parameters: dateOfBirth={}, phone={}, fullName={}, email={}", dateOfBirth, phone, fullName, email);

        UserSearchRequest request = UserSearchRequest.builder()
                .dateOfBirth(dateOfBirth)
                .phone(phone)
                .fullName(fullName)
                .email(email)
                .build();

        return userService.searchUsers(request, pageable);
    }
}
