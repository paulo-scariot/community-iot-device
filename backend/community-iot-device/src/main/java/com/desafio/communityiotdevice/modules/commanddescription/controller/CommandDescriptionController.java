package com.desafio.communityiotdevice.modules.commanddescription.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.commanddescription.dto.CommandDescriptionRequest;
import com.desafio.communityiotdevice.modules.commanddescription.dto.CommandDescriptionResponse;
import com.desafio.communityiotdevice.modules.commanddescription.service.CommandDescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commanddescription")
@AllArgsConstructor
public class CommandDescriptionController {

    private final CommandDescriptionService commandDescriptionService;

    @GetMapping
    public Page<CommandDescriptionResponse> list(@RequestParam(required = false) String filter,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size){
        return commandDescriptionService.getCommandDescriptions(filter, page, size);
    }

    @GetMapping("{id}")
    public CommandDescriptionResponse get(@PathVariable Integer id) {
        return CommandDescriptionResponse.of(commandDescriptionService.findById(id));
    }

    @PostMapping
    public CommandDescriptionResponse save(@RequestBody CommandDescriptionRequest request) {
        return commandDescriptionService.save(request);
    }

    @PutMapping("{id}")
    public CommandDescriptionResponse update(@RequestBody CommandDescriptionRequest request,
                               @PathVariable Integer id) {
        return commandDescriptionService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return commandDescriptionService.delete(id);
    }
}
