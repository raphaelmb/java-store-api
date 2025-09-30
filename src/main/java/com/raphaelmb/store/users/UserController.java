package com.raphaelmb.store.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Gets all users", responses = {
            @ApiResponse(responseCode = "200"),
    })
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sort) {
        if (!Set.of("name", "email").contains(sort)) sort = "name";

        var users = userService.getAllUsers(sort);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets user by ID", responses = {
            @ApiResponse()
    })
    public ResponseEntity<UserDto> getUser(@PathVariable @Positive Long id) {
        var user = userService.getUserByID(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Registers a new user", responses = {
            @ApiResponse()
    })
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder) {
        var userDto = userService.registerUser(request);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a user", responses = {
            @ApiResponse()
    })
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") @Positive Long id, @Valid @RequestBody UpdateUserRequest request) {
        var user = userService.updateUser(id, request);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes user", responses = {
            @ApiResponse()
    })
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") @Positive Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    @Operation(summary = "Changes user's password", responses = {
            @ApiResponse(),
    })
    public ResponseEntity<Void> changePassword(@PathVariable @Positive Long id, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);

        return ResponseEntity.noContent().build();
    }
}
