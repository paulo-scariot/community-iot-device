package com.desafio.communityiotdevice.modules.commanddescription.dto;

import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.commanddescription.model.CommandDescription;
import lombok.Data;

@Data
public class CommandDescriptionResponse {

    private Integer id;
    private String operation;
    private String description;
    private String result;
    private String format;
    private CommandResponse command;

    public static CommandDescriptionResponse of(CommandDescription commandDescription) {
        CommandDescriptionResponse response = new CommandDescriptionResponse();
        response.setId(commandDescription.getId());
        response.setOperation(commandDescription.getOperation());
        response.setDescription(commandDescription.getDescription());
        response.setResult(commandDescription.getResult());
        response.setFormat(commandDescription.getFormat());
        response.setCommand(CommandResponse.of(commandDescription.getCommand()));
        return response;
    }
}
