package org.nn.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
public class VehicleDataPointId implements Serializable {
    private String plateNumber;
    private LocalTime entryTime;
    private LocalTime exitTime;

}
