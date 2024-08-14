package com.example.demo.domain.security.auth;

import com.example.demo.domain.security.dto.AuthRequest;
import com.example.demo.domain.security.dto.AuthResponseDTO;
import com.example.demo.domain.user.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;
    @Operation(summary = "Request a Token")

    @ApiResponses(value = {

            @ApiResponse(responseCode = "201",
                    description = "Token generated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserInfo.class))),

            @ApiResponse(responseCode = "401",
                    description = "Not Authorized",
                    content = @Content(mediaType = "application/json"))

    })
    @PostMapping(value = "/authenticate",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<AuthResponseDTO> authenticate(@io.swagger.v3.oas.annotations.parameters.RequestBody AuthRequest authRequest) {
        log.info("/authenticate called");

        return authService.authenticateUser(authRequest);

    }



}
