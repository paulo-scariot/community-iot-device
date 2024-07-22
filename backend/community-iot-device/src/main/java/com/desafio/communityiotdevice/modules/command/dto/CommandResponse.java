package com.desafio.communityiotdevice.modules.command.dto;

import com.desafio.communityiotdevice.modules.command.model.Command;
import lombok.Data;

@Data
public class CommandResponse {

    private Integer id;
    private String command;
    private String description;

    public static CommandResponse of(Command command) {
        CommandResponse response = new CommandResponse();
        response.setId(command.getId());
        response.setCommand(command.getCommand());
        response.setDescription(command.getDescription());
        return response;
    }
}
