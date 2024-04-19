package com.udyogi.config;

import com.udyogi.employeemodule.entities.Authority;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Configuration
public class UdyogiUsernamePwdAuthenticationProvider implements AuthenticationProvider {
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;

    public UdyogiUsernamePwdAuthenticationProvider(EmployeeRepo employeeRepo, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       String username = authentication.getName();
         String password = authentication.getCredentials().toString();
        EmployeeEntity employeeEntity = employeeRepo.findByEmail(username);
        if(passwordEncoder.matches(password, employeeEntity.getPassword())){
return new UsernamePasswordAuthenticationToken(username,password,getGrantedAuthorities(employeeEntity.getAuthorities()));
        }else{
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
