package com.desafio.communityiotdevice.modules.device.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.device.dto.DeviceResponse;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
@AllArgsConstructor
@Tag(name = "APIs de Dispositivo")
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Busca lista paginada de despositivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<DeviceResponse> list(@RequestParam(required = false) String filter,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int rpp){
        return deviceService.getDevices(filter, page, rpp);
    }

    @Operation(summary = "Busca o dispositivo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"Device with id 1 not found\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceResponse get(@PathVariable Integer id) {
        return DeviceResponse.of(deviceService.findById(id));
    }

    @Operation(summary = "Salva o dispositivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispostivo salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceResponse save(@RequestBody DeviceRequest request) {
        return deviceService.save(request);
    }


    @Operation(summary = "Edita o dispositivo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo editado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @PutMapping(value ="{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeviceResponse update(@RequestBody DeviceRequest request,
                                  @PathVariable Integer id) {
        return deviceService.update(request, id);
    }

    @Operation(summary = "Deleta o dispositivo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse delete(@PathVariable Integer id) {
        return deviceService.delete(id);
    }
}
