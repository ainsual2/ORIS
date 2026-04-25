package org.example.weatherservice.service;

import org.example.weatherservice.model.MachineStatus;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MachineStatusService {

    private Map<Integer, MachineStatus> statusMap = new HashMap<>();
    private Long startTime;

    public MachineStatusService() {
        startTime = new Date().getTime();
    }

    public MachineStatus getStatus(int id) {
        MachineStatus machineStatus = statusMap.get(id);
        if (machineStatus.getResource() < 0) return new MachineStatus();
        Long d = new Date().getTime() - startTime;

        machineStatus.setTemp1(machineStatus.getTemp1() + d / 1_000);
        machineStatus.setTemp2(machineStatus.getTemp2() + Math.sin(d / 1_000));
        machineStatus.setTemp3(machineStatus.getTemp3() + d / 20_000);
        machineStatus.setPressure(2.5 * machineStatus.getTemp2() / 12.5);
        machineStatus.setResource(machineStatus.getResource() - d / 1_000);
        return machineStatus;
    }

    public void updateStatus(int id, MachineStatus machineStatus) {
        statusMap.put(id, machineStatus);
    }

}
