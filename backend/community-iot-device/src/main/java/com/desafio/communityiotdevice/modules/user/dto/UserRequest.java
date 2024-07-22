package com.desafio.communityiotdevice.modules.user.dto;

import com.desafio.communityiotdevice.modules.user.model.RoleEnum;
import lombok.Data;

@Data
public class UserRequest {

    private Integer id;
    private String username;
    private String password;
    private RoleEnum role;

}
