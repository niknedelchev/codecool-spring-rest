package org.nn.repository;

import org.nn.entity.VehicleDataPoint;
import org.nn.entity.VehicleDataPointId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDataPointRepository extends JpaRepository<VehicleDataPoint, VehicleDataPointId> {

}
