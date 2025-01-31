package com.desafio.communityiotdevice.modules.command.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.command.dto.CommandRequest;
import com.desafio.communityiotdevice.modules.command.dto.CommandResponse;
import com.desafio.communityiotdevice.modules.command.service.CommandService;
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
@RequestMapping("/command")
@AllArgsConstructor
@Tag(name = "APIs de Comando")
public class CommandController {

    private final CommandService commandService;

    @Operation(summary = "Busca lista paginada de comandos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CommandResponse> list(@RequestParam(required = false) String filter,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int rpp){
        return commandService.getCommands(filter, page, rpp);
    }

    @Operation(summary = "Busca o comando pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"Command with id 1 not found\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandResponse get(@PathVariable Integer id){
        return CommandResponse.of(commandService.findById(id));
    }

    @Operation(summary = "Salva o comando")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comando salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandResponse save(@RequestBody CommandRequest request) {
        return commandService.save(request);
    }

    @Operation(summary = "Edita o comando pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comando editado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @PutMapping(value ="{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandResponse update(@RequestBody CommandRequest request,
                               @PathVariable Integer id) {
        return commandService.update(request, id);
    }

    @Operation(summary = "Deleta o comando pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comando deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse delete(@PathVariable Integer id) {
        return commandService.delete(id);
    }
}
