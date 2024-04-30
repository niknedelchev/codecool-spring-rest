package org.nn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(VehicleDataPointId.class)
public class VehicleDataPoint {

    @Id
    private String plateNumber;
    @Id
    private LocalTime entryTime;
    @Id
    private LocalTime exitTime;



}
