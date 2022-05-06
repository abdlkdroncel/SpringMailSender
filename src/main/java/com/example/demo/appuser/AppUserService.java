package com.example.demo.appuser;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {


    private final AppRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return repository.findByEmail(mail).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    public String signUpUser(AppUser appUser){
       boolean userExists =repository.findByEmail(appUser.getEmail()).isPresent();
       if(userExists){
           throw new IllegalStateException("Email already taken");

       }
       String encodedPassword=bCryptPasswordEncoder.encode(appUser.getPassword());
       appUser.setPassword(encodedPassword);
       repository.save(appUser);
        // TODO: send confirmantion token
        String token=UUID.randomUUID().toString();
        ConfirmationToken confirmationToken=new ConfirmationToken(
               token
                , LocalDateTime.now()
                ,LocalDateTime.now().plusMinutes(15),
                appUser
        );
        tokenService.saveConfirmationToken(confirmationToken);
        //TODO:sen email
        return token;
    }

    public int enableAppUser(String email) {
        return repository.enableAppUser(email);
    }
}
