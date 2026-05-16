package org.example.weatherservice.service;

import org.example.weatherservice.model.MachineStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MachineStatusService {

    private final Map<Integer, MachineStatus> statusMap = new HashMap<>();
    private final Long startTime;

    public MachineStatusService() {
        startTime = new Date().getTime();
    }

    public MachineStatus getStatus(int id) {
        MachineStatus machineStatus = statusMap.computeIfAbsent(id, this::buildDefaultStatus);
        if (machineStatus.getResource() < 0) {
            return machineStatus;
        }

        Long d = new Date().getTime() - startTime;

        machineStatus.setTemp1(machineStatus.getTemp1() + d / 1_000d);
        machineStatus.setTemp2(machineStatus.getTemp2() + Math.sin(d / 1_000d));
        machineStatus.setTemp3(machineStatus.getTemp3() + d / 20_000d);
        machineStatus.setPressure(2.5 * machineStatus.getTemp2() / 12.5);
        machineStatus.setResource(machineStatus.getResource() - d / 1_000d);
        return machineStatus;
    }

    public void updateResource(int id, double resource) {
        MachineStatus machineStatus = statusMap.computeIfAbsent(id, this::buildDefaultStatus);
        machineStatus.setResource(resource);
    }

    public void updateStatus(int id, MachineStatus machineStatus) {
        statusMap.put(id, machineStatus);
    }

    private MachineStatus buildDefaultStatus(int id) {
        MachineStatus status = new MachineStatus();
        status.setTemp1(25.0 + (id % 3));
        status.setTemp2(22.0 + (id % 2));
        status.setTemp3(20.0);
        status.setPressure(1.2);
        status.setResource(100.0);
        return status;
    }
}