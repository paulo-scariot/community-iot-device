package com.desafio.communityiotdevice.modules.measurement.controller;

import com.desafio.communityiotdevice.modules.measurement.dto.MeasurementResponse;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/measurement")
@AllArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping
    public Page<MeasurementResponse> list(@RequestParam(required = false) String filter,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int rpp){
        return measurementService.getMeasurements(filter, page, rpp);
    }

//    @GetMapping("{id}")
//    public MeasurementResponse get(@PathVariable Integer id){
//        return MeasurementResponse.of(measurementService.findById(id));
//    }
//
//    @PostMapping
//    public MeasurementResponse save(@RequestBody MeasurementRequest request) {
//        return measurementService.save(request);
//    }
//
//    @PutMapping("{id}")
//    public MeasurementResponse update(@RequestBody MeasurementRequest request,
//                                      @PathVariable Integer id) {
//        return measurementService.update(request, id);
//    }
//
//    @DeleteMapping("{id}")
//    public SuccessResponse delete(@PathVariable Integer id) {
//        return measurementService.delete(id);
//    }
}
