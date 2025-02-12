package com.desafio.communityiotdevice.modules.measurement.controller;

import com.desafio.communityiotdevice.modules.measurement.dto.MeasurementResponse;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/measurement")
@AllArgsConstructor
@Tag(name = "APIs de Medida")
public class MeasurementController {

    private final MeasurementService measurementService;


    @Operation(summary = "Busca lista paginada de medidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<MeasurementResponse> list(@RequestParam(required = false) String filter,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int rpp){
        return measurementService.getMeasurements(filter, page, rpp);
    }

}
