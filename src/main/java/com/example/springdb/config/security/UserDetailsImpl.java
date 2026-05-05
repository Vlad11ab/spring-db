package com.example.springdb.config.security;


import com.example.springdb.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsImpl implements UserDetailsService {
    UserRepository userRepository;

    public UserDetailsImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmailIgnoreCaseJPQL(email)
                .orElseThrow(()-> new UsernameNotFoundException("User with email " + email + " not found"));
    }
}
