package com.desafio.communityiotdevice.modules.command.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.command.service.CommandService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command")
@AllArgsConstructor
public class CommandController {

    private final CommandService commandService;

    @GetMapping
    public Page<CommandResponse> list(@RequestParam(required = false) String filter,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size){
        return commandService.getCommands(filter, page, size);
    }

    @GetMapping("{id}")
    public CommandResponse get(@PathVariable Integer id){
        return CommandResponse.of(commandService.findById(id));
    }

    @PostMapping
    public CommandResponse save(@RequestBody CommandRequest request) {
        return commandService.save(request);
    }

    @PutMapping("{id}")
    public CommandResponse update(@RequestBody CommandRequest request,
                               @PathVariable Integer id) {
        return commandService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return commandService.delete(id);
    }
}
