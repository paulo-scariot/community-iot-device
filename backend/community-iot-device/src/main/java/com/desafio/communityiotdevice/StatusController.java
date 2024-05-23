package com.desafio.communityiotdevice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class StatusController {

    @GetMapping("/")
    public ResponseEntity<HashMap<String, Object>> status() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("service", "Community IoT Device API");
        map.put("status", "OK");
        return ResponseEntity.ok(map);
    }
}
