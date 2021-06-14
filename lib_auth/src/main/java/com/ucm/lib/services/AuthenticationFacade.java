package com.ucm.lib.services;

import com.ucm.lib.config.util.JwtUtil;
import com.ucm.lib.dao.UserDAO;
import com.ucm.lib.entities.User;
import com.ucm.lib.entities.UserInfo;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Allows access to currently logged in user via dependency injection. May be used
 * in services rather than only controllers.
 */
@Service
public class AuthenticationFacade implements IAuthenticationFacade {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDAO userDAO;

    @Autowired
    public AuthenticationFacade(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDAO userDAO) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDAO = userDAO;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Authenticates the credentials.
     * If the username and password are valid, the user will be logged in successfully, the authentication will
     * be set in the context, and true will be returned.
     * If the combination is not valid, the context is not touched, and false will be returned.
     * @param username The user's username.
     * @param password The user's password.
     * @return True if the user has been logged in, false otherwise.
     */
    public Boolean authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (BadCredentialsException e) {
            return false;
        }
    }

    /**
     * Generate a token for the currently logged in user. Throws AuthenticationCredentialsNotFoundException if
     * no user is logged in.
     * @return A JWT token.
     */
    public String generateToken() {
        Authentication authentication = getAuthentication();
        if(authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("No user is currently logged in.");
        }
        return jwtUtil.generateJwtToken(authentication);
    }

    /**
     * Get a UserInfo object for the currently logged in user.Throws AuthenticationCredentialsNotFoundException if
     * no user is logged in.
     * @return A UserInfo of the currently logged in user.
     */
    public UserInfo getUserInfo() {
        Authentication authentication = getAuthentication();
        if(authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("No user is currently logged in.");
        }
        return (UserInfo) authentication.getPrincipal();
    }

    /**
     * Get the database User entity for the currently logged in user.
     * Keep in mind that if you have an existing UserInfo object, changes made to this object won't be reflected in it.
     * Throws AuthenticationCredentialsNotFoundException if no user is logged in.
     * @return A User entity.
     */
    public User getUserEntity() {
        Authentication authentication = getAuthentication();
        if(authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("No user is currently logged in.");
        }
        return userDAO.getById(getUserInfo().getId());
    }
}
