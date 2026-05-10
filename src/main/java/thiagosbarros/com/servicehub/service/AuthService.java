package thiagosbarros.com.servicehub.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import thiagosbarros.com.servicehub.controller.dto.LoginRequest;
import thiagosbarros.com.servicehub.controller.dto.LoginResponse;
import thiagosbarros.com.servicehub.security.JwtService;
import thiagosbarros.com.servicehub.security.ServiceHubUserDetails;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        String principal = request.empresaId() + "|" + request.email();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(principal, request.senha())
        );

        ServiceHubUserDetails userDetails = (ServiceHubUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token, "Bearer");
    }
}
