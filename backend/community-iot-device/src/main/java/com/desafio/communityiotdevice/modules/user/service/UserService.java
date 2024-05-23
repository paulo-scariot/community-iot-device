package com.desafio.communityiotdevice.modules.user.service;

import com.desafio.communityiotdevice.config.exception.ValidationException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.user.dto.UserRequest;
import com.desafio.communityiotdevice.modules.user.dto.UserResponse;
import com.desafio.communityiotdevice.modules.user.model.User;
import com.desafio.communityiotdevice.modules.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor_ = { @Lazy })
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Lazy
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
                .orElseThrow(() -> new ValidationException("User with id " + id + " not found"));
    }

    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username);
    }

    public UserResponse save(UserRequest request) {
        validateUserData(request);
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        return UserResponse.of(userRepository.save(user));
    }

    public UserResponse update(UserRequest request,
                                  Integer id) {
        validateUserData(request);
        validateId(id);
        var user = User.of(request);
        user.setId(id);
        userRepository.save(user);
        return UserResponse.of(user);
    }

    public SuccessResponse delete(Integer id) {
        validateId(id);
        userRepository.deleteById(id);
        return SuccessResponse.create("User with id " + id + " has been deleted");
    }

    private void validateUserData(UserRequest request) {
        if (isEmpty(request.getUsername())){
            throw new ValidationException("The username cannot be empty");
        }
        if (isEmpty(request.getPassword())){
            throw new ValidationException("The password cannot be empty");
        }
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The user id cannot be empty");
        }
    }
}
