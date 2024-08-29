package com.training.library.service;

import com.training.library.model.AuthenticatedUser;
import com.training.library.model.User;
import com.training.library.repo.UserRepository;
import com.training.library.util.JwtUtil;
import jdk.jfr.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    public String registerUser(User user) {
        var hashedPassword = passwordEncoder.encode(user.getPassword());
        this.createNewUser(new User(user.getUsername(), hashedPassword));


        return "User registered successfully";
    }

    public String loginUser(String username, String password) {

        User user;
        try {
            // TODO: Optimize login query, with current setup, this service and custom user details service will perform same query.
            user = getUserByUsername(username);
            var authToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid credential");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    public User getUserByUsername(String username) throws IllegalArgumentException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with username %s is not found", username)));
    }

    public User getUserById(Long id) throws IllegalArgumentException {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with id %s is not found", id)));
    }

    public User createNewUser(User newUserData) {
        if (userRepository.findByUsername(newUserData.getUsername()).isPresent())
            throw new IllegalArgumentException("Username is already registered");

        return userRepository.save(new User(newUserData.getUsername(), newUserData.getPassword()));
    }

    public List<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Operation not permitted");
        }

    }
}
