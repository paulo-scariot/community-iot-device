package com.desafio.communityiotdevice.modules.user.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.user.dto.LoginResponse;
import com.desafio.communityiotdevice.modules.user.dto.UserRequest;
import com.desafio.communityiotdevice.modules.user.dto.UserResponse;
import com.desafio.communityiotdevice.modules.user.service.UserService;
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
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "APIs de Usuário")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Log in através do usuário e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log in realizado com sucesso", content = @Content(examples = @ExampleObject(value = "{ \"credentials\": \"base64credencial\" }"))),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"Invalid username or password\" }"))),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }

    @Operation(summary = "Busca lista paginada de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UserResponse> list(@RequestParam(required = false) String filter,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int rpp){
        return userService.getUsers(filter, page, rpp);
    }

    @Operation(summary = "Busca o usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"User with id 1 not found\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse get(@PathVariable Integer id) {
        return UserResponse.of(userService.findById(id));
    }

    @Operation(summary = "Salva o usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(examples = @ExampleObject(value = "{ \"status\": 403, \"message\": \"You are not allow to do this action!\" }"))),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse save(@RequestBody UserRequest request) {
        return userService.save(request);
    }

    @Operation(summary = "Edita o usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário editado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(examples = @ExampleObject(value = "{ \"status\": 403, \"message\": \"You are not allow to do this action!\" }"))),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @PutMapping(value ="{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse update(@RequestBody UserRequest request,
                                  @PathVariable Integer id) {
        return userService.update(request, id);
    }

    @Operation(summary = "Deleta o usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos", content = @Content(examples = @ExampleObject(value = "{ \"status\": 400, \"message\": \"The field cannot be empty\" }"))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(examples = @ExampleObject(value = "{ \"status\": 403, \"message\": \"You are not allow to do this action!\" }"))),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse delete(@PathVariable Integer id) {
        return userService.delete(id);
    }
}
