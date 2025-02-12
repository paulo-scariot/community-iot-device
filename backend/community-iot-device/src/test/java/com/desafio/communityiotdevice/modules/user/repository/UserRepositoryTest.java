package com.desafio.communityiotdevice.modules.user.repository;

import com.desafio.communityiotdevice.modules.user.model.RoleEnum;
import com.desafio.communityiotdevice.modules.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        MockitoAnnotations.openMocks(this);

        user = new User("teste", "teste", RoleEnum.ADMIN, Collections.emptyList());

    }

    @Test
    void testFindById_WhenUserExists_ThenReturnUser() {

        // Given / Arrange
        userRepository.save(user);

        // When / Act
        User findUser = userRepository.findById(user.getId()).get();

        // Then / Assert
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void testFindAll_WhenUsersExists_ThenReturnUserList() {
        // Given / Arrange
        userRepository.save(user);

        // When / Act
        List<User> users = userRepository.findAll();

        // Then / Assert
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).extracting(User::getUsername).contains("teste");
    }

    @Test
    void testFindByUsername_WhenUsernameMatchesFilter_ThenReturnUser() {
        // Given / Arrange
        userRepository.save(user);

        // When / Act
        User user1 = userRepository.findByUsername(user.getUsername());

        // Then / Assert
        assertThat(user1).isNotNull();
        assertThat(user1.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testFindByUserContainingIgnoreCase_WhenUserMatchesFilter_ThenReturnPaginatedUser() {
        // Given / Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String filter = "teste2";
        user.setUsername(filter);
        userRepository.save(user);

        // When / Act
        Page<User> page = userRepository.findByUsernameContainingIgnoreCase(filter, pageable);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList()).extracting(User::getUsername).contains("teste2");
    }

    @Test
    void testSave_WhenUserIsSaved_ThenReturnSavedUser() {
        // Given / Arrange

        // When / Act
        User save = userRepository.save(user);

        // Then / Assert
        assertThat(save).isNotNull();
        assertThat(save.getId()).isGreaterThan(0);
    }

    @Test
    void testSave_WhenUserIsUpdated_ThenReturnUpdatedUser() {

        // Given / Arrange
        user.setId(1);
        User save = userRepository.save(user);

        // When / Act
        User findUser = userRepository.findById(save.getId()).get();
        findUser.setUsername("teste1111");
        User updated = userRepository.save(findUser);

        // Then / Assert
        assertThat(updated).isNotNull();
        assertThat(updated.getUsername()).isEqualTo("teste1111");
    }

    @Test
    void testDeleteById_WhenUserExists_ThenDeleteUserAndReturnEmptyOptionalUser() {

        // Given / Arrange
        userRepository.save(user);

        // When / Act
        userRepository.deleteById(user.getId());
        Optional<User> byId = userRepository.findById(user.getId());

        // Then / Assert
        assertThat(byId).isEmpty();
    }
}