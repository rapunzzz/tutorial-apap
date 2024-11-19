package apap.tutorial.manpromanpro.restcontroller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.tutorial.manpromanpro.restdto.request.LoginJwtRequestDTO;
import apap.tutorial.manpromanpro.restdto.response.BaseResponseDTO;
import apap.tutorial.manpromanpro.restdto.response.LoginJwtResponseDTO;
import apap.tutorial.manpromanpro.security.jwt.JwtUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> addUser(@RequestBody LoginJwtRequestDTO loginRequest) {
        var baseResponseDTO = new BaseResponseDTO<LoginJwtResponseDTO>();
        try {
            // Create authentication token
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                );

            // Authenticate using AuthenticationManagerBuilder
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername());

            // Prepare response with DTO
            LoginJwtResponseDTO responseData = new LoginJwtResponseDTO(jwt);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Login berhasil!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(responseData);

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (AuthenticationException e) {
            baseResponseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponseDTO.setMessage("Username atau Password salah!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.UNAUTHORIZED);
        }
    }
}

