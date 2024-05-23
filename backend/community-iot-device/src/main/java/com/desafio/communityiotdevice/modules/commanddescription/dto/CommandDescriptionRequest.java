package com.desafio.communityiotdevice.modules.commanddescription.dto;

import lombok.Data;

@Data
public class CommandDescriptionRequest {

    private String operation;
    private String description;
    private String result;
    private String format;
    private Integer commandId;
}
