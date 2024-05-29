package com.desafio.communityiotdevice.modules.parameter.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.parameter.dto.ParameterRequest;
import com.desafio.communityiotdevice.modules.parameter.dto.ParameterResponse;
import com.desafio.communityiotdevice.modules.parameter.service.ParameterService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameter")
@AllArgsConstructor
public class ParameterController {

    private final ParameterService parameterService;

    @GetMapping
    public Page<ParameterResponse> list(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int rpp){
        return parameterService.getParameters(page, rpp);
    }

    @GetMapping("{id}")
    public ParameterResponse get(@PathVariable Integer id) {
        return ParameterResponse.of(parameterService.findById(id));
    }

    @PostMapping
    public ParameterResponse post(@RequestBody ParameterRequest request) {
        return parameterService.save(request);
    }

    @PutMapping("{id}")
    public ParameterResponse update(@RequestBody ParameterRequest request,
                               @PathVariable Integer id) {
        return parameterService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return parameterService.delete(id);
    }
}
