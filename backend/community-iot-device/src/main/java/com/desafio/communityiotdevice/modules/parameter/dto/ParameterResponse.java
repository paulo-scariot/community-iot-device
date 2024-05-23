package com.desafio.communityiotdevice.modules.parameter.dto;

import com.desafio.communityiotdevice.modules.parameter.model.Parameter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ParameterResponse {

    private Integer id;
    private String name;
    private String description;

    public static ParameterResponse of(Parameter parameter) {
        ParameterResponse parameterResponse = new ParameterResponse();
        BeanUtils.copyProperties(parameter, parameterResponse);
        return parameterResponse;
    }
}
