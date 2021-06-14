package com.ucm.demo.controler;

import com.ucm.demo.dto.RequestLoginDto;
import com.ucm.demo.dto.ResponseLoginDto;
import com.ucm.lib.entities.User;
import com.ucm.lib.entities.UserInfo;
import com.ucm.lib.services.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains example usage of the lib_auth library
 * Last updated to reflect version 0.0.5-SNAPSHOT
 * @author Joshua Podhola
 */
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    //This service is what we'll be using to handle A&A.
    //So long as the request has a bearer token in its header, Spring Security will automatically
    //handle authenticating the user for us, as long as a resource requires it.
    final AuthenticationFacade authenticationFacade;

    @Autowired
    public AuthController(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Example login function using the lib_auth library.
     * @param userRequest A UserRequest DTO.
     * @return A ResponseLoginDto containing future authentication information.
     */
    @PostMapping("/signin")
    @PreAuthorize("isAnonymous()") //Only unauthenticated users can access this resource.
    public ResponseEntity<ResponseLoginDto> authenticateUser(@Valid @RequestBody RequestLoginDto userRequest) {
        if(authenticationFacade.authenticate(userRequest.getUsername(), userRequest.getPassword())) {
            String jwtToken = authenticationFacade.generateToken();
            UserInfo userInfo = authenticationFacade.getUserInfo();
            return ResponseEntity.ok(
                    new ResponseLoginDto(
                            jwtToken,
                            userInfo.getId(),
                            userInfo.getUsername(),
                            userInfo.getEmail(),
                            userInfo.getPhNum(),
                            userInfo.getFirstName(),
                            userInfo.getLastName()
                    )
            );
        }
        //You can use an ExceptionControllerAdvice class to handle these and return prettier errors to the front end.
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password credentials.");
    }
    /**
     * Example as to how to get information about the currently logged in user.
     * @return The username and email of the logged in user.
     */
    @PreAuthorize("isAuthenticated()") //SpEL to only allow logged in users.
    @GetMapping("/")
    public Map<String, String> get() {
        //Two different ways to get information about the logged in user.
        User user = authenticationFacade.getUserEntity(); //This is what's stored in the database
        UserInfo userInfo = authenticationFacade.getUserInfo(); //This is what Spring Security cares about
        assert user.getUsername().equals(userInfo.getUsername()); //Just to show they are indeed the same user.
        //You can get a UserInfo from a User by using UserInfo.build(User user), which is a static method.

        //This is just an easy way to put together a JSON response.
        Map<String, String> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        return map;
    }

    /**
     * This method requires the user to be logged in AND have the User role (rather than the Admin role)
     * @return Simple response
     */
    @RolesAllowed("ROLE_USER")
    @GetMapping("/user")
    public String userOnly() {
        return "hi user!";
    }

    /**
     * Requires the user to be authenticated and have the Admin role.
     * @return Simple response
     */
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminOnly() {
        return "hi admin!";
    }

    /**
     * Requires an account to be email-verified before it may access this. It checks to see if is_active is TRUE.
     * @return Simple response.
     */
    @RolesAllowed("ROLE_VERIFIED")
    @GetMapping("/verified")
    public String verifiedOnly() {
        return "hi verified user!";
    }

    /**
     * This allows anyone that is either verified OR an admin.
     * @return Simple response.
     */
    @RolesAllowed({"ROLE_VERIFIED", "ROLE_ADMIN"})
    @GetMapping("/verified_or_admin")
    public String verifiedOrAdmin() {
        return "hi verified user!";
    }
}
