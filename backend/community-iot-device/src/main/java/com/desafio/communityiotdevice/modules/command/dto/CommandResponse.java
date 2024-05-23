package com.desafio.communityiotdevice.modules.command.dto;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.parameter.dto.ParameterResponse;
import lombok.Data;

import java.util.List;

@Data
public class CommandResponse {

    private Integer id;
    private String command;
    private List<ParameterResponse> parameters;

    public static CommandResponse of(Command command) {
        CommandResponse response = new CommandResponse();
        response.setId(command.getId());
        response.setCommand(command.getCommand());
        response.setParameters(command.getParameters().stream().map(ParameterResponse::of).toList());
        return response;
    }
}
