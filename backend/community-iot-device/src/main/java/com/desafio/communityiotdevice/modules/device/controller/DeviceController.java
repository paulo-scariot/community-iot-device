package com.desafio.communityiotdevice.modules.device.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.device.dto.DeviceResponse;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
@AllArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public Page<DeviceResponse> list(@RequestParam(required = false) String filter,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int rpp){
        return deviceService.getDevices(filter, page, rpp);
    }

    @GetMapping("{id}")
    public DeviceResponse get(@PathVariable Integer id) {
        return DeviceResponse.of(deviceService.findById(id));
    }

    @PostMapping
    public DeviceResponse save(@RequestBody DeviceRequest request) {
        return deviceService.save(request);
    }

    @PutMapping("{id}")
    public DeviceResponse update(@RequestBody DeviceRequest request,
                                  @PathVariable Integer id) {
        return deviceService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return deviceService.delete(id);
    }
}
