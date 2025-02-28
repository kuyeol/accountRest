package org.acme.reactive.crud.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.reactive.crud.domain.Authority;
import org.acme.reactive.crud.domain.User;
import org.acme.reactive.crud.security.PasswordHasher;
import org.acme.reactive.crud.service.dto.UserDTO;
import org.acme.reactive.crud.security.AuthoritiesConstants;
import org.jose4j.jwk.Use;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@Transactional
public class AccountService {

    final PasswordHasher passwordHasher;

    @Inject
    public AccountService(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }


    public User registerUser(UserDTO userDTO, String password) {
        User newUser = new User();
        newUser.email = userDTO.email;
        newUser.password = PasswordHasher.bcrypt(password);
        Set<Authority> authorities = new HashSet<>();
        Authority.<Authority> findByIdOptional(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.authorities = authorities;
        User.persist(newUser);
        return newUser;


    }


}
