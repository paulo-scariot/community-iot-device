package com.desafio.communityiotdevice.modules.user.repository;

import com.desafio.communityiotdevice.modules.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    Page<User> findByUsernameContainingIgnoreCase(String filter, Pageable pageable);
}
