package com.stage.elearning.security.utility;

import com.stage.elearning.exceptions.ResourceNotFoundException;
import com.stage.elearning.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private  final UserEntityRepository userEntityRepository;
    @Autowired
    public CustomUserDetailsService(UserEntityRepository userEntityRepository)
    {
        this.userEntityRepository = userEntityRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userEntityRepository.fetchUserWithEmail(email).orElseThrow(()-> new ResourceNotFoundException("The email address provided could not be found in our system."));
    }
}
