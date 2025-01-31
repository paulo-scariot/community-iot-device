package com.desafio.communityiotdevice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class StatusController {

    @Operation(summary = "Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status", content = @Content(examples = @ExampleObject(value = "{ \"service\": \"Community IoT Device API\", \"status\": \"OK\" }"))),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados", content = @Content(schema = @Schema())),
    })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> status() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("service", "Community IoT Device API");
        map.put("status", "OK");
        return ResponseEntity.ok(map);
    }
}
