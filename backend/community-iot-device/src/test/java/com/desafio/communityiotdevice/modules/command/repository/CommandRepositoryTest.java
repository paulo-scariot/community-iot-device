package com.desafio.communityiotdevice.modules.command.repository;

import com.desafio.communityiotdevice.modules.command.model.Command;
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
class CommandRepositoryTest {

    @Autowired
    private CommandRepository commandRepository;

    private Command command;

    @BeforeEach
    public void setup() {
        // Given / Arrange
        MockitoAnnotations.openMocks(this);

        command = new Command("teste", "teste", Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void testFindById_WhenCommandExists_ThenReturnCommand() {

        // Given / Arrange
        commandRepository.save(command);

        // When / Act
        Command findCommand = commandRepository.findById(command.getId()).get();

        // Then / Assert
        assertThat(findCommand).isNotNull();
        assertThat(findCommand.getId()).isEqualTo(command.getId());
    }

    @Test
    void testFindAll_WhenCommandsExists_ThenReturnCommandList() {
        // Given / Arrange
        commandRepository.save(command);

        // When / Act
        List<Command> commands = commandRepository.findAll();

        // Then / Assert
        assertThat(commands).isNotNull();
        assertThat(commands.size()).isEqualTo(1);
        assertThat(commands).extracting(Command::getDescription).contains("teste");
        assertThat(commands).extracting(Command::getCommand).contains("teste");
    }

    @Test
    void testFindByCommandContainingIgnoreCase_WhenCommandMatchesFilter_ThenReturnPaginatedCommand() {
        // Given / Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String filter = "teste2";
        command.setCommand(filter);
        commandRepository.save(command);

        // When / Act
        Page<Command> page = commandRepository.findByCommandContainingIgnoreCase(filter, pageable);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList()).extracting(Command::getCommand).contains("teste2");
    }

    @Test
    void testSave_WhenCommandIsSaved_ThenReturnSavedCommand() {
        // Given / Arrange

        // When / Act
        Command save = commandRepository.save(command);

        // Then / Assert
        assertThat(save).isNotNull();
        assertThat(save.getId()).isGreaterThan(0);
    }

    @Test
    void testSave_WhenCommandIsUpdated_ThenReturnUpdatedCommand() {

        // Given / Arrange
        command.setId(1);
        Command save = commandRepository.save(command);

        // When / Act
        Command findCommand = commandRepository.findById(save.getId()).get();
        findCommand.setCommand("teste1111");
        Command updated = commandRepository.save(findCommand);

        // Then / Assert
        assertThat(updated).isNotNull();
        assertThat(updated.getCommand()).isEqualTo("teste1111");
    }

    @Test
    void testDeleteById_WhenCommandExists_ThenDeleteCommandAndReturnEmptyOptionalCommand() {

        // Given / Arrange
        commandRepository.save(command);

        // When / Act
        commandRepository.deleteById(command.getId());
        Optional<Command> byId = commandRepository.findById(command.getId());

        // Then / Assert
        assertThat(byId).isEmpty();
    }

}