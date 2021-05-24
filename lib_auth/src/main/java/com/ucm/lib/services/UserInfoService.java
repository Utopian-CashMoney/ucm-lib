package com.ucm.lib.services;


import com.ucm.lib.entities.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucm.lib.dao.UserDAO;
import com.ucm.lib.entities.User;

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
