package com.ucm.demo.controler;

import com.ucm.demo.dto.RequestLoginDto;
import com.ucm.demo.dto.ResponseLoginDto;
import com.ucm.lib.config.util.JwtUtil;
import com.ucm.lib.dao.UserDAO;
import com.ucm.lib.entities.User;
import com.ucm.lib.entities.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    final AuthenticationManager authenticationManager;
    final UserDAO userRepository;
    final JwtUtil jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDAO userRepository, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody RequestLoginDto userRequest) {
//        if(userRepository.existsByUsername(userRequest.getUsername())) {
//            User user = userRepository.findByUsername(userRequest.getUsername());
//            Boolean isActive = user.getActive();
//            if(!isActive) {
//                throw new Error("Please confirm the account first via email sent to you!");
//            }
//        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserInfo userDetails = (UserInfo) authentication.getPrincipal();

        return ResponseEntity.ok(
                new ResponseLoginDto(
                        jwtToken,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getPhNum(),
                        userDetails.getFirstName(),
                        userDetails.getLastName()
                )
        );
    }

    @RolesAllowed("ROLE_USER")
    @GetMapping("/user")
    public String userOnly() {
        return "hi user!";
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminOnly() {
        return "hi admin!";
    }
}
