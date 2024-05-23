package ru.ainur.bank.service.user;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ainur.bank.dto.request.user.UserRegistrationRequest;
import ru.ainur.bank.dto.request.user.UserSearchRequest;
import ru.ainur.bank.dto.response.UserDto;
import ru.ainur.bank.dto.response.UserSearchResponse;
import ru.ainur.bank.exception.UserUpdateException;
import ru.ainur.bank.model.QUser;
import ru.ainur.bank.model.User;
import ru.ainur.bank.repository.UserRepository;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(UserRegistrationRequest request) {
        User user = getUserFromUserRegistrationRequest(request);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadCredentialsException("Username is already taken.");
        }

        validateUser(user);

        return userRepository.save(user);
    }


    public void changeContactInfo(Consumer<User> updater) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new UserUpdateException("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();
        updater.accept(user);
        validateUser(user);

        userRepository.save(user);
    }

    public UserSearchResponse searchUsers(UserSearchRequest request, Pageable pageable) {
        BooleanBuilder builder = getBooleanBuilderFromUserSearchRequest(request);

        Page<User> usersPage = userRepository.findAll(builder, pageable);

        List<UserDto> userDtos = usersPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return UserSearchResponse.builder()
                .users(userDtos)
                .totalUsers(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .build();
    }

    private static BooleanBuilder getBooleanBuilderFromUserSearchRequest(UserSearchRequest request) {
        BooleanBuilder builder = new BooleanBuilder();
        QUser user = QUser.user;
        if (request.getDateOfBirth() != null) {
            builder.and(user.dateOfBirth.goe(request.getDateOfBirth()));
        }
        if (request.getPhone() != null) {
            builder.and(user.phone.eq(request.getPhone()));
        }
        if (request.getFullName() != null) {
            builder.and(user.fullName.startsWithIgnoreCase(request.getFullName()));
        }
        if (request.getEmail() != null) {
            builder.and(user.email.eq(request.getEmail()));
        }
        return builder;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null && user.getPhone() == null) {
            throw new UserUpdateException("Email and phone number can not be both empty");
        }
        if (user.getEmail() != null && userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
            throw new UserUpdateException("Email %s is already in use".formatted(user.getEmail()));
        }
        if (user.getPhone() != null && userRepository.existsByPhoneAndIdNot(user.getPhone(), user.getId())) {
            throw new UserUpdateException("Phone number %s is already in use".formatted(user.getEmail()));
        }
    }

    private UserDto convertToDTO(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    private User getUserFromUserRegistrationRequest(UserRegistrationRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .build();
    }
}
