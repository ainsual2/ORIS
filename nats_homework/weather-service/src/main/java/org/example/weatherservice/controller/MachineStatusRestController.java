package org.example.weatherservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.weatherservice.model.MachineStatus;
import org.example.weatherservice.service.MachineStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MachineStatusRestController {

    private final MachineStatusService service;

    @GetMapping("/api/status/{id}")
    public ResponseEntity<MachineStatus> getMachineStatus(
            @PathVariable("id") Integer id
    ) {
        return ResponseEntity.ok(service.getStatus(id));
    }

    @PostMapping("/api/resource")
    public ResponseEntity<String> getResource(
            @RequestParam("resource") Double resource, @RequestParam("id") Integer id
    ) {
        return ResponseEntity.ok("{\"status\":\"success\"}");
    }

}
