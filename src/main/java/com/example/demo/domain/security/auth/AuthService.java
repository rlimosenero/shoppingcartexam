package com.example.demo.domain.security.auth;

import com.example.demo.domain.security.dto.AuthRequest;
import com.example.demo.domain.security.dto.AuthResponseDTO;
import com.example.demo.domain.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<AuthResponseDTO> authenticateUser(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return buildErrorResponse("Not Authorized", "Failed");
        } catch (Exception ex) {
            return buildErrorResponse("Unexpected error", "Failed");
        }

        String token = jwtService.generateToken(authRequest.getUsername());
        return buildSuccessResponse(token, "Token Generated");
    }

    private ResponseEntity<AuthResponseDTO> buildErrorResponse(String message, String status) {
        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken("")
                .message(message)
                .status(status)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    private ResponseEntity<AuthResponseDTO> buildSuccessResponse(String token, String message) {
        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken(token)
                .message(message)
                .status("Success")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

