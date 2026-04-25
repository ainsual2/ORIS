package org.example.weatherservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineStatus {
    private Double temp1;
    private Double temp2;
    private Double temp3;
    private Double pressure;
    private Double resource;
}
