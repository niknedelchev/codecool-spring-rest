package org.nn.service;

import org.nn.entity.VehicleDataPoint;
import org.nn.repository.VehicleDataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataHandlerService {
    public static final int DISTANCE = 10;
    public static final double HOUR_IN_MILIS = 3600000.00;
    public static final int VALID_ROW_DATA_PIECES_COUNT = 9;

    private VehicleDataPointRepository vehicleDataRepository;

    @Autowired
    public DataHandlerService(VehicleDataPointRepository vehicleDataRepository) {
        this.vehicleDataRepository = vehicleDataRepository;
    }

    /**
     * Task 1: Read and store the data of file measurements.txt.
     */
    public List<VehicleDataPoint> saveDataPoints(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();

            List<VehicleDataPoint> dataPoints = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .filter(line -> line.split(" ").length == VALID_ROW_DATA_PIECES_COUNT)
                    .map(this::parseDataLineToVehicleDataPoint)
                    .collect(Collectors.toList());

            vehicleDataRepository.saveAll(dataPoints);
            return dataPoints;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Task 2: Display the number of vehicles whose data were recorded in the measurement on the screen.
     */
    public long getRecordedVehiclesNumber() {
        return vehicleDataRepository.count();
    }

    /**
     * Task 3: From the available data, determine the number of vehicles that passed the exit point of the section before 9 o'clock.
     */
    public long getVehiclePassedBeforeNineOClock() {
        return vehicleDataRepository.findAll().stream()
                .filter(dataPoint -> dataPoint.getExitTime()
                        .isBefore(LocalTime.of(9, 0))).count();
    }

    private LocalTime getHourMinute(String hourMin) {
        if (!hourMin.matches("\\d+\\s\\d+")) {
            throw new InputMismatchException(String.format("Invalid input %s. Please provide hour and min in following format: H m"));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H m");
        return LocalTime.parse(hourMin, formatter);
    }

    /**
     * Task 4.a: Determine the number of vehicles that passed the entry point in the given minute.
     * If in the given minute no vehicle passed the entry point, then display value 0.
     */
    public long displayNumberOfVehiclesThatPassedEntryPoint(String hourMin) {
        LocalTime time = getHourMinute(hourMin);
        return vehicleDataRepository.findAll().stream()
                .filter(dataPoint -> dataPoint.getEntryTime().getHour() == time.getHour()
                        && dataPoint.getEntryTime().getMinute() == time.getMinute())
                .count();

    }

    /**
     * Task 4.b: Determine the traffic intensity, which is the number of vehicle on the road section in the minute that starts at the given minute
     * (e.g. if the given hour minute is 08:09, then between 08:09:00.000-08:09:59.999) divided by the length of the road section.
     * Display the value in decimal fraction format.
     */
    public String displayTraficIntensity(String hourMin) {
        LocalTime time = getHourMinute(hourMin);
        double trafficIntensity = (double) (vehicleDataRepository.findAll().stream()
                .filter(dataPoint -> dataPoint.getExitTime().isAfter(time)
                        && dataPoint.getEntryTime().isBefore(time.plusMinutes(1))))
                .count() / DISTANCE;
        return "4.b. The traffic intensity: " + String.format("%.2f", trafficIntensity);
    }

    /**
     * Task 5: Find the speed of the vehicle with the highest average speed and the number of vehicles overtaken by it in the measured section.
     * If there are several highest average speeds, it is enough to display only one of them.
     * Display the license plate number of the vehicle, the average speed as an integer and the number of overtaken vehicles.
     */
    public String displayDataOfVehicleWithHighestSpeed() {
        System.out.println("Excercise 5.");

        VehicleDataPoint vehicle = vehicleDataRepository.findAll().stream()
                .sorted(byAverageSpeedReversed)
                .findFirst()
                .get();

        int highestAverageSpeed = getAverageSpeed(vehicle.getEntryTime(), vehicle.getExitTime());
        long overpassedVehicles = vehicleDataRepository.findAll().stream()
                .filter(dataPoint -> dataPoint.getEntryTime().isBefore(vehicle.getEntryTime())
                        && dataPoint.getExitTime().isAfter(vehicle.getExitTime()))
                .count();

     return String.format("""
                The data of the vehicle with the highest speed are
                license plate number: %s
                average speed: %d km/h
                number of overtaken vehicles: %d
                                """, vehicle.getPlateNumber(), highestAverageSpeed, overpassedVehicles);
    }

    public String displayPercentOfVehiclesThatWereSpeeding() {
        System.out.println("Excercise 6.");
        double percentSpeedingVehicles = (double) vehicleDataRepository.findAll().stream()
                .filter(vehicleDataPoint -> this.getAverageSpeed(vehicleDataPoint.getEntryTime(), vehicleDataPoint.getExitTime()) >= 90)
                .count() / vehicleDataRepository.count() * 100;

        return String.format("%.2f", percentSpeedingVehicles) + "% percent of the vehicles were speeding.";
    }

    private VehicleDataPoint parseDataLineToVehicleDataPoint(String dataLine) {
        String[] lineArr = dataLine.split(" ");
        return new VehicleDataPoint(lineArr[0], getTime(1, lineArr), getTime(5, lineArr));
    }

    private LocalTime getTime(int start, String[] lineArr) {
        int hour = Integer.parseInt(lineArr[start]);
        int minute = Integer.parseInt(lineArr[++start]);
        int second = Integer.parseInt(lineArr[++start]);
        int nanoSecond = Integer.parseInt(lineArr[++start]);

        return LocalTime.of(hour, minute, second, nanoSecond);
    }

    private int getAverageSpeed(LocalTime entryTime, LocalTime exitTime) {
        double elapsedTime = ChronoUnit.MILLIS.between(entryTime, exitTime) / HOUR_IN_MILIS;
        return (int) (DISTANCE / elapsedTime);
    }

    private Comparator<VehicleDataPoint> byAverageSpeedReversed = new Comparator<VehicleDataPoint>() {
        @Override
        public int compare(VehicleDataPoint dataPoint1, VehicleDataPoint dataPoint2) {
            return getAverageSpeed(dataPoint2.getEntryTime(), dataPoint2.getExitTime()) -
                    getAverageSpeed(dataPoint1.getEntryTime(), dataPoint1.getExitTime());
        }
    };

}
