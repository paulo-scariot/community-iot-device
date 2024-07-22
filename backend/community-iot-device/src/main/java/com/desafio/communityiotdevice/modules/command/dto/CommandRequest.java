package com.desafio.communityiotdevice.modules.command.dto;

import lombok.Data;

@Data
public class CommandRequest {

    private Integer id;
    private String command;
    private String description;
}
