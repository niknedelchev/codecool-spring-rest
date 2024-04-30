package org.nn.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nn.entity.VehicleDataPoint;
import org.nn.response.Message;
import org.nn.service.DataHandlerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class VehicleDataPointsController {

    DataHandlerService dataHandlerService;

    //Task 1: Read and store the data of file measurements.txt.
    @PostMapping(path = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Message> saveEmployee(@RequestPart MultipartFile file) {

        List<VehicleDataPoint> saveDataPoints = dataHandlerService.saveDataPoints(file);
        log.info("Done saving. {}", saveDataPoints);

        return new ResponseEntity<>(new Message(String.format("Saved data points: %s", saveDataPoints)),
                HttpStatus.CREATED);
    }

    //Task 2: Display the number of vehicles whose data were recorded in the measurement on the screen.
    @GetMapping(path = "getRecordedVehiclesNumber")
    public ResponseEntity<Message> getRecordedVehiclesNumber() {

        long recordedVehiclesNumber = dataHandlerService.getRecordedVehiclesNumber();
        log.info("Display the number of vehicles whose data were recorded in the measurement on the screen: {}", recordedVehiclesNumber);

        return new ResponseEntity<>(new Message(String.format("Excercise 2. The data of %d vehicles were recorded in the measurement.",
                recordedVehiclesNumber)), HttpStatus.OK);
    }

    //Task 3: From the available data, determine the number of vehicles that passed the exit point
    @GetMapping(path = "getVehiclePassedBeforeNineOClock")
    public ResponseEntity<Message> getVehiclePassedBeforeNineOClock() {

        long vehiclesPassedBeforeNine = dataHandlerService.getVehiclePassedBeforeNineOClock();
        log.info("From the available data, determine the number of vehicles that passed the exit point: {}", vehiclesPassedBeforeNine);

        return new ResponseEntity<>(new Message(String.format("Excercise 3.Before 9 o'clock %d vehicles passed the exit point recorder.",
                vehiclesPassedBeforeNine)), HttpStatus.OK);
    }

    //Task 4.a: Determine the number of vehicles that passed the entry point in the given minute.
    @PostMapping(path = "getVehiclesNumberThatPassedInAGivenMinute")
    public ResponseEntity<Message> getNumberOfVehiclesThatPassedInAGivenMinute(@RequestParam String userTime) {
        log.info("Task 4.a: Determine the number of vehicles that passed the entry point in the given minute.");

        String message = String.format("4.a. The number of vehicle that passed the entry point recorder: %d",
                dataHandlerService.displayNumberOfVehiclesThatPassedEntryPoint(userTime));
        log.info(message);

        return new ResponseEntity<>(new Message(message), HttpStatus.OK);
    }

    // Task 4.b: Determine the traffic intensity
    @PostMapping(path = "getTrafficIntensity")
    public ResponseEntity<Message> getTrafficIntensity(@RequestParam String userTime) {
        log.info("Task 4.b: Determine the traffic intensity");
        return new ResponseEntity<>(new Message(dataHandlerService.displayTraficIntensity(userTime)), HttpStatus.OK);
    }

    //Task 5: Find the speed of the vehicle with the highest average speed and the number of vehicles overtaken by it.
    @GetMapping(path = "getDataOfVehicleWithHighestSpeed")
    public ResponseEntity<String> getDataOfVehicleWithHighestSpeed() {
        log.info("Task 5: Find the speed of the vehicle with the highest average speed and the number of vehicles overtaken by it.");
        return new ResponseEntity<>(dataHandlerService.displayDataOfVehicleWithHighestSpeed(), HttpStatus.OK);
    }

    //Task 6: Determine what percent of the vehicles exceeded the maximum speed limit on the measured section (90 km/h) with their average speed. Display the result in decimal number format according to the example.
    @GetMapping(path = "getPercentOfVehiclesThatWereSpeeding")
    public ResponseEntity<Message> getPercentOfVehiclesThatWereSpeeding() {
        log.info("Determine what percent of the vehicles exceeded the maximum speed limit on the measured section (90 km/h) with their average speed. Display the result in decimal number format according to the example.");
        return new ResponseEntity<>(new Message(dataHandlerService.displayPercentOfVehiclesThatWereSpeeding()), HttpStatus.OK);
    }

}
