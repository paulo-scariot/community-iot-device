package com.desafio.communityiotdevice.modules.user.dto;

import com.desafio.communityiotdevice.modules.user.model.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserResponse {

    private Integer id;
    private String username;

    public static UserResponse of(User user) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }
}
