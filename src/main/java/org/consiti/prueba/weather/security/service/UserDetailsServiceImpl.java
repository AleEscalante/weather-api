package org.consiti.prueba.weather.security.service;

import org.consiti.prueba.weather.security.entity.PrincipalUser;
import org.consiti.prueba.weather.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getOneByEmail(email).get();
        return new PrincipalUser(user);
    }
}
