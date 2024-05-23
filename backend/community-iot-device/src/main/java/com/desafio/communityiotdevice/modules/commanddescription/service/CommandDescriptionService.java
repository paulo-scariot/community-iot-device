package com.desafio.communityiotdevice.modules.commanddescription.service;

import com.desafio.communityiotdevice.config.exception.ValidationException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.service.CommandService;
import com.desafio.communityiotdevice.modules.commanddescription.dto.CommandDescriptionRequest;
import com.desafio.communityiotdevice.modules.commanddescription.dto.CommandDescriptionResponse;
import com.desafio.communityiotdevice.modules.commanddescription.model.CommandDescription;
import com.desafio.communityiotdevice.modules.commanddescription.repository.CommandDescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class CommandDescriptionService {

    private final CommandDescriptionRepository commandDescriptionRepository;

    @Lazy
    private final CommandService commandService;

    public Page<CommandDescriptionResponse> getCommandDescriptions(String filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommandDescription> commandDescriptionPage;
        if (filter != null && !isEmpty(filter)) {
            commandDescriptionPage = commandDescriptionRepository.findByOperationContainingIgnoreCase(filter, pageable);
        } else {
            commandDescriptionPage = commandDescriptionRepository.findAll(pageable);
        }
        return commandDescriptionPage.map(CommandDescriptionResponse::of);
    }

    public CommandDescription findById(Integer id) {
        return commandDescriptionRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Command description with id " + id + " not found"));
    }

    public CommandDescriptionResponse save(CommandDescriptionRequest request) {
        validateCommandDescriptionData(request);
        var command = commandService.findById(request.getCommandId());
        CommandDescription commandDescription = CommandDescription.of(request, command);
        CommandDescription save = commandDescriptionRepository.save(commandDescription);
        return CommandDescriptionResponse.of(save);
    }

    public CommandDescriptionResponse update(CommandDescriptionRequest request, Integer id) {
        validateCommandDescriptionData(request);
        validateId(id);
        var command = commandService.findById(request.getCommandId());
        CommandDescription commandDescription = CommandDescription.of(request, command);
        commandDescription.setId(id);
        CommandDescription save = commandDescriptionRepository.save(commandDescription);
        return CommandDescriptionResponse.of(save);
    }

    public SuccessResponse delete(Integer id) {
        validateId(id);
        commandDescriptionRepository.deleteById(id);
        return SuccessResponse.create("Command description with id " + id + " has been deleted");
    }

    public Boolean existsByCommandId(Integer id) {
        return commandDescriptionRepository.existsByCommandId(id);
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The command description id cannot be empty");
        }
    }

    private void validateCommandDescriptionData(CommandDescriptionRequest request) {
        if (isEmpty(request.getOperation())){
            throw new ValidationException("Operation required");
        }
        if (isEmpty(request.getDescription())){
            throw new ValidationException("Description required");
        }
        if (isEmpty(request.getResult())){
            throw new ValidationException("Result required");
        }
        if (isEmpty(request.getFormat())){
            throw new ValidationException("Format required");
        }
        if (isEmpty(request.getCommandId())){
            throw new ValidationException("CommandId required");
        }
    }

}