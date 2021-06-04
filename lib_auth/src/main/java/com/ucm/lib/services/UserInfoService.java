package com.ucm.lib.services;


import com.ucm.lib.entities.UserInfo;
import com.ucm.lib.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucm.lib.dao.UserDAO;
import com.ucm.lib.entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserInfoService implements UserDetailsService {
    final
    UserDAO userRepository;

    @Autowired
    public UserInfoService(UserDAO userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }

        return UserInfo.build(user);
    }
}
