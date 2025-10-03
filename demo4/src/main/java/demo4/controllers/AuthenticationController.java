package demo4.controllers;

import demo4.entity.User;
import demo4.models.LoginResponse;
import demo4.models.LoginUserModel;
import demo4.models.RegisterUserModel;
import demo4.services.AuthenticationService;
import demo4.services.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<User> register(@RequestBody RegisterUserModel registerUser) {
        User registeredUser = authenticationService.signup(registerUser);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping(path="/login")
    @Transactional
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserModel loginUser) {
        User authenticatedUser = authenticationService.authenticate(loginUser);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}