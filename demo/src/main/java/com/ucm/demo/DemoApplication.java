package com.ucm.demo;

import com.ucm.lib.services.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mainly exists for integration testing the package structure. Will be repurposed later.
 */
@SpringBootApplication(scanBasePackages = "com.ucm")
@RestController
public class DemoApplication {
    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public DemoApplication(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Returns a simple page to display the user and their authorization level. For testing.
     * @author Joshua Podhola
     */
    @GetMapping("/")
    @ResponseBody
    public String home() {
        //In the event of a successful authentication, get it. In the case of no such successful authentication,
        //a 401 error will immediately be thrown to the user.
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getName();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
