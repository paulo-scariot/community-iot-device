package com.desafio.communityiotdevice.modules.parameter.service;

import com.desafio.communityiotdevice.config.exception.ValidationException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.service.CommandService;
import com.desafio.communityiotdevice.modules.parameter.dto.ParameterRequest;
import com.desafio.communityiotdevice.modules.parameter.dto.ParameterResponse;
import com.desafio.communityiotdevice.modules.parameter.model.Parameter;
import com.desafio.communityiotdevice.modules.parameter.repository.ParameterRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class ParameterService {

    private final ParameterRepository parameterRepository;

    @Lazy
    private final CommandService commandService;

    public Page<ParameterResponse> getParameters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Parameter> parameterPage = parameterRepository.findAll(pageable);
        return parameterPage.map(ParameterResponse::of);
    }

    public Parameter findById(Integer id) {
        return parameterRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Parameter with id " + id + " not found"));
    }

    public ParameterResponse save(ParameterRequest request) {
        validateParameterData(request);
        var command = commandService.findById(request.getCommandId());
        Parameter save = parameterRepository.save(Parameter.of(request, command));
        return ParameterResponse.of(save);
    }

    public ParameterResponse update(ParameterRequest request,
                               Integer id) {
        validateParameterData(request);
        validateId(id);
        var command = commandService.findById(request.getCommandId());
        var parameter = Parameter.of(request, command);
        parameter.setId(id);
        Parameter save = parameterRepository.save(parameter);
        return ParameterResponse.of(save);
    }

    public SuccessResponse delete(Integer id) {
        validateId(id);
        parameterRepository.deleteById(id);
        return SuccessResponse.create("Parameter with id " + id + " has been deleted");
    }

    private void validateParameterData(ParameterRequest request) {
        if (isEmpty(request.getName())){
            throw new ValidationException("The name cannot be empty");
        }
        if (isEmpty(request.getDescription())){
            throw new ValidationException("The description cannot be empty");
        }
        if (isEmpty(request.getCommandId())){
            throw new ValidationException("The command id cannot be empty");
        }
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The parameter id cannot be empty");
        }
    }
}
