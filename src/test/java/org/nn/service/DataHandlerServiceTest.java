package org.nn.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.nn.entity.VehicleDataPoint;
import org.nn.repository.VehicleDataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DataHandlerServiceTest {

    public static final int DATA_POINTS_COUNT = 687;
    @Mock
    private VehicleDataPointRepository vehicleDataRepository;
    @Autowired
    private DataHandlerService dataHandlerService;

    //TODO: Add tests

    @SneakyThrows
    @Test
    void givenMultipartFile_whenSaveDataPointsIsCalled_thenDataIsReadAndSaved() {
        //Arrange
        MultipartFile file =  new MockMultipartFile("measurements.txt", new FileInputStream(new File("src/test/resources/measurements.txt")));

        //Act
        List<VehicleDataPoint> actualVehicleDataPoints = dataHandlerService.saveDataPoints(file);

        //Assert
        Assertions.assertEquals(DATA_POINTS_COUNT, actualVehicleDataPoints.size());
    }

    @SneakyThrows
    @Test
    void getRecordedVehiclesNumber() {
        //Arrange
        MultipartFile file =  new MockMultipartFile("measurements.txt", new FileInputStream(new File("src/test/resources/measurements.txt")));
        List<VehicleDataPoint> actualVehicleDataPoints = dataHandlerService.saveDataPoints(file);
        Mockito.when(vehicleDataRepository.findAll()).thenReturn(actualVehicleDataPoints);

        //Act
        //Assert
        Assertions.assertEquals(687, dataHandlerService.getRecordedVehiclesNumber());
    }

    @Test
    void getVehiclePassedBeforeNineOClock() {
        //Arrange
        //Act
        //Assert
    }

    @Test
    void displayNumberOfVehiclesThatPassedEntryPoint() {
    }

    @Test
    void displayTraficIntensity() {
    }

    @Test
    void displayDataOfVehicleWithHighestSpeed() {
    }

    @Test
    void displayPercentOfVehiclesThatWereSpeeding() {
    }
}