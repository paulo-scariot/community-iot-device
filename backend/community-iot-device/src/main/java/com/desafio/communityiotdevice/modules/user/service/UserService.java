package com.desafio.communityiotdevice.modules.user.service;

import com.desafio.communityiotdevice.config.exception.CustomHttpException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.user.dto.LoginResponse;
import com.desafio.communityiotdevice.modules.user.dto.UserRequest;
import com.desafio.communityiotdevice.modules.user.dto.UserResponse;
import com.desafio.communityiotdevice.modules.user.model.RoleEnum;
import com.desafio.communityiotdevice.modules.user.model.User;
import com.desafio.communityiotdevice.modules.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor_ = { @Lazy })
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Lazy
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Lazy
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username);

        if (userDetails == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .build();
    }

    public LoginResponse login(UserRequest userRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }
        String auth = userRequest.getUsername() + ":" + userRequest.getPassword();
        return new LoginResponse(Base64.getEncoder().encodeToString(auth.getBytes()));
    }

    public Page<UserResponse> getUsers(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        if (filter != null && !isEmpty(filter)) {
            userPage = userRepository.findByUsernameContainingIgnoreCase(filter, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        return userPage.map(UserResponse::of);
    }

    public User findById(Integer id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "User with id " + id + " not found"));
    }

    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username);
    }

    public UserResponse save(UserRequest request) {
        validateIsAdmin();
        validateUserRequest(request);
        User byUsername = findByUsername(request.getUsername());
        if (byUsername != null) {
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "User with username: " + request.getUsername() + " already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        return UserResponse.of(userRepository.save(user));
    }

    public UserResponse update(UserRequest request,
                                  Integer id) {
        validateIsAdmin();
        validateUserRequest(request);
        validateId(id);
        findById(id);
        var user = User.of(request);
        user.setId(id);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return UserResponse.of(user);
    }

    public SuccessResponse delete(Integer id) {
        validateIsAdmin();
        validateId(id);
        findById(id);
        userRepository.deleteById(id);
        return SuccessResponse.create("User with id " + id + " has been deleted");
    }

    private void validateUserRequest(UserRequest request) {
        if (isEmpty(request.getUsername())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The username cannot be empty");
        }
        if (isEmpty(request.getPassword())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The password cannot be empty");
        }
        if (isEmpty(request.getRole())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The role cannot be empty");
        }
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The user id cannot be empty");
        }
    }

    private void validateIsAdmin(){
        User userByContext = findUserByContext();
        if (userByContext.getRole().equals(RoleEnum.USER)){
            throw new CustomHttpException(HttpStatus.FORBIDDEN, "You are not allow to do this action!");
        }
    }

    public User findUserByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                return findByUsername(username);
            }
        }
        throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Username not found");
    }

}
