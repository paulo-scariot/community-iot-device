package com.desafio.communityiotdevice.modules.command.service;

import com.desafio.communityiotdevice.config.exception.ValidationException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.command.repository.CommandRepository;
import com.desafio.communityiotdevice.modules.commanddescription.service.CommandDescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class CommandService {

    private final CommandRepository commandRepository;

    @Lazy
    private final CommandDescriptionService commandDescriptionService;

    public Page<CommandResponse> getCommands(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Command> commandPage;
        if (filter != null && !isEmpty(filter)) {
            commandPage = commandRepository.findByCommandContainingIgnoreCase(filter, pageable);
        } else {
            commandPage = commandRepository.findAll(pageable);
        }
        return commandPage.map(CommandResponse::of);
    }

    public Command findById(Integer id) {
        return commandRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Command with id " + id + " not found"));
    }

    public CommandResponse save(CommandRequest request) {
        validateCommandData(request);
        Command save = commandRepository.save(Command.of(request));
        return CommandResponse.of(save);
    }

    public CommandResponse update(CommandRequest request,
                               Integer id) {
        validateCommandData(request);
        validateId(id);
        var command = Command.of(request);
        command.setId(id);
        Command save = commandRepository.save(command);
        return CommandResponse.of(save);
    }

    public SuccessResponse delete(Integer id) {
        validateId(id);
        if (commandDescriptionService.existsByCommandId(id)){
            throw new ValidationException("The command with id " + id + " is used in a command description");
        }
        commandRepository.deleteById(id);
        return SuccessResponse.create("Command with id " + id + " has been deleted");
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The command id cannot be empty");
        }
    }

    private void validateCommandData(CommandRequest request) {
        if (isEmpty(request.getCommand())){
            throw new ValidationException("Command required");
        }
    }
}
