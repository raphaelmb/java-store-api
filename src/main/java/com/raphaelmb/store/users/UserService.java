package com.raphaelmb.store.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers(String sort) {
        return userRepository.findAll(Sort.by(sort)).stream().map(userMapper::toDto).toList();
    }

    public UserDto getUserByID(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        return userMapper.toDto(user);
    }

    public User getCurrentUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new EmailAlreadyRegisteredException();

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(id);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.getOldPassword())) throw new UserNotAuthorizedException();

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    public UserDto getUserByEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        return userMapper.toDto(user);
    }
}
