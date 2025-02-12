package com.desafio.communityiotdevice.modules.command.service;

import com.desafio.communityiotdevice.config.exception.CustomHttpException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.command.repository.CommandRepository;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;

class CommandServiceTest {

    @Mock
    private CommandRepository commandRepository;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private DeviceService deviceService;

    @Mock
    private Command mockCommand;

    @InjectMocks
    private CommandService commandService;

    private Command command;

    private CommandRequest commandRequest;

    @BeforeEach
    public void setup() {
        // Arrange
        MockitoAnnotations.openMocks(this);

        command = new Command("teste", "teste", Collections.emptyList(), Collections.emptyList());
        commandRequest = new CommandRequest();
        commandRequest.setCommand("teste");
        commandRequest.setDescription("teste");
    }

    @Test
    void testFindById_WhenCommandExists_ThenReturnCommand() {

        // Given / Arrange
        given(commandRepository.findById(anyInt())).willReturn(Optional.of(command));

        // When / Act
        Command findCommand = commandService.findById(1);

        // Then / Assert
        assertThat(findCommand).isNotNull();
        assertThat(findCommand.getCommand()).isEqualTo("teste");
    }

    @Test
    void testGetCommands_WhenValidRequestWithNullFilter_ThenReturnPaginatedCommandResponse() {

        // Given / Arrange
        List<Command> commands = List.of(command);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Command> mockPage = new PageImpl<>(commands, pageable, commands.size());
        given(commandRepository.findAll(pageable)).willReturn(mockPage);

        // When / Act
        Page<CommandResponse> page = commandService.getCommands(null, 0, 10);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList())
                .anyMatch(command -> command.getCommand().equals("teste"));
    }

    @Test
    void testGetCommands_WhenValidRequestWithFilter_ThenReturnPaginatedCommandResponse() {

        // Given / Arrange
        String filter = "teste2";
        command.setCommand(filter);
        List<Command> commands = List.of(command);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Command> mockPage = new PageImpl<>(commands, pageable, commands.size());
        given(commandRepository.findByCommandContainingIgnoreCase("teste2", pageable)).willReturn(mockPage);

        // When / Act
        Page<CommandResponse> page = commandService.getCommands(filter, 0, 10);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList())
                .anyMatch(command -> command.getCommand().equals("teste2"));
    }

    @Test
    void testGetCommands_WhenValidRequestWithEmptyFilter_ThenReturnPaginatedCommandResponse() {

        // Given / Arrange
        List<Command> commands = List.of(command);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Command> mockPage = new PageImpl<>(commands, pageable, commands.size());
        given(commandRepository.findAll(pageable)).willReturn(mockPage);

        // When / Act
        Page<CommandResponse> page = commandService.getCommands("", 0, 10);

        // Then / Assert
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.toList())
                .anyMatch(command -> command.getCommand().equals("teste"));
    }

    @Test
    void testSave_WhenValidCommandRequest_ThenReturnCommandResponse() {

        // Given / Arrange
        given(commandRepository.save(any(Command.class))).willAnswer(invocation -> {
            Command save = invocation.getArgument(0);
            save.setId(1);
            return save;
        });

        // When / Act
        CommandResponse commandResponse = commandService.save(commandRequest);

        // Then / Assert
        assertThat(commandResponse).isNotNull();
        assertThat(commandResponse.getCommand()).isEqualTo("teste");
    }

    @Test
    void testSave_WhenCommandIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveCommand() {

        // Given / Arrange
        CommandRequest commandRequest1 = new CommandRequest();
        commandRequest1.setDescription("teste");

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            commandService.save(commandRequest1);
        });

        // Then / Assert
        then(commandRepository).should(never()).save(any(Command.class));
        assertThat(exception.getMessage()).isEqualTo("Command required");
    }

    @Test
    void testSave_WhenDescriptionIsEmpty_ThenThrowCustomHttpExceptionAndDoNotSaveCommand() {

        // Given / Arrange
        CommandRequest commandRequest1 = new CommandRequest();
        commandRequest1.setCommand("teste");

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            commandService.save(commandRequest1);
        });

        // Then / Assert
        then(commandRepository).should(never()).save(any(Command.class));
        assertThat(exception.getMessage()).isEqualTo("Description required");
    }

    @Test
    void testUpdate_WhenValidCommandRequestAndId_ThenReturnCommandResponse() {

        // Given / Arrange
        given(commandRepository.save(any(Command.class))).willAnswer(invocation -> {
            Command save = invocation.getArgument(0);
            save.setId(1);
            return save;
        });

        // When / Act
        CommandResponse commandResponse = commandService.update(commandRequest, 1);

        // Then / Assert
        assertThat(commandResponse).isNotNull();
        assertThat(commandResponse.getCommand()).isEqualTo("teste");
    }

    @Test
    void testUpdate_WhenIdIsNull_ThenThrowCustomHttpExceptionAndDoNotUpdateCommand() {

        // Given / Arrange

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            commandService.update(commandRequest, null);
        });

        // Then / Assert
        then(commandRepository).should(never()).save(any(Command.class));
        assertThat(exception.getMessage()).isEqualTo("The command id cannot be empty");
    }

    @Test
    void testDelete_WhenCommandExists_ThenReturnSucessMessage() {

        // Given / Arrange
        command.setId(1);
        given(commandRepository.findById(anyInt())).willReturn(Optional.of(command));
        given(deviceService.existsByCommandId(anyInt())).willReturn(false);
        willDoNothing().given(commandRepository).deleteById(anyInt());

        // When / Act
        SuccessResponse successResponse = commandService.delete(command.getId());

        // Then / Assert
        then(commandRepository).should(times(1)).deleteById(command.getId());
        assertThat(successResponse.getMessage()).isEqualTo("Command with id 1 has been deleted");

    }

    @Test
    void testDelete_WhenDeviceExistsByCommandId_ThenThrowCustomHttpExceptionAndDoNotDeleteCommand() {

        // Given / Arrange
        command.setId(1);
        given(deviceService.existsByCommandId(anyInt())).willReturn(true);

        // When / Act
        CustomHttpException exception = assertThrows(CustomHttpException.class, () -> {
            commandService.delete(command.getId());
        });

        // Then / Assert
        then(commandRepository).should(never()).deleteById(anyInt());
        assertThat(exception.getMessage()).isEqualTo("The command with id 1 is used in a device");
    }

}