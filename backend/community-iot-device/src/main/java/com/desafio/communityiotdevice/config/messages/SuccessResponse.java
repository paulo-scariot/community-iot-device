package com.desafio.communityiotdevice.config.messages;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class SuccessResponse {

    private Integer status;
    private String message;

    public static SuccessResponse create(String message) {
        return SuccessResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }
}
