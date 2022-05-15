package com.service;

import com.entity.User;
import com.repository.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecUserDetails implements UserDetailsService {
    @Autowired
    private UserInterface userInterface;
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userInterface.getUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        user.setRoles(userInterface.getRolesByUserId(user.getUsername()));
        return user;
    }
}
