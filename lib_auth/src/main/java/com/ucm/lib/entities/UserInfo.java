package com.ucm.lib.entities;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ucm.lib.entities.User;

public class UserInfo implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Integer id;

    private final String username;

    private final String email;

    private final String phNum;

    private final String firstName;

    private final String lastName;

    private final String password;

    private Collection<? extends GrantedAuthority> authorities;


    public UserInfo(Integer id, String username, String email, String password, String phNum, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phNum = phNum;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static UserInfo build(User user) {

        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPhNum(),
                user.getFirstName(),
                user.getLastName());


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhNum() {
        return phNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserInfo user = (UserInfo) o;
        return Objects.equals(id, user.id);
    }
}


