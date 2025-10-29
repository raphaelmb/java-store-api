package com.raphaelmb.store.auth;

import com.raphaelmb.store.users.LoginRequest;
import com.raphaelmb.store.users.UserDto;
import com.raphaelmb.store.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final JwtService jwtService;
    private final CookieService cookieService;

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var tokens = authService.generateTokens(request.getEmail());
        var accessToken = tokens.accessToken();
        var refreshToken = tokens.refreshToken();

        var cookie = cookieService.createRefreshTokenCookie(refreshToken.toString());
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refreshes JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userService.getUserByID(jwt.getUserId());
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var user = userService.getUserByID(userId);

        return ResponseEntity.ok(user);
    }
}
