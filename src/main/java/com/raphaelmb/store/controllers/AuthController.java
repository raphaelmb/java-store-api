package com.raphaelmb.store.controllers;

import com.raphaelmb.store.config.JwtConfig;
import com.raphaelmb.store.dtos.JwtResponse;
import com.raphaelmb.store.dtos.LoginRequest;
import com.raphaelmb.store.dtos.UserDto;
import com.raphaelmb.store.services.AuthService;
import com.raphaelmb.store.services.JwtService;
import com.raphaelmb.store.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthService authService;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var tokens = authService.generateToken(request.getEmail());
        var accessToken = tokens.getFirst();
        var refreshToken = tokens.getLast();

        var cookie = createCookie(refreshToken);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jwtService.getUserIdFromToken(refreshToken);
        var user = userService.getUserByID(userId);
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var user = userService.getUserByID(userId);

        return ResponseEntity.ok(user);
    }

    private Cookie createCookie(String refreshToken) {
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        // cookie.setSecure(true);

        return cookie;
    }
}
