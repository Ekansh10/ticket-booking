package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
    @JsonProperty("train_id")
    private String trainId;
    @JsonProperty("train_name")
    private String trainName;
    @JsonProperty("train_no")
    private String trainNo;
    private List<List<Integer>> seats;
    @JsonProperty("station_times")
    private Map<String, String> stationTimes;
    private List<String> stations;

    // Constructor
    public Train(){
    }

    public Train(String trainId, String trainName, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes, List<String> stations) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
    }

    // Methods
    public String getTrainInfo(){
        String trainTimings = this.getStationTimes().keySet().stream().map(key -> key.toUpperCase() + ": " + this.getStationTimes().get(key)).collect(Collectors.joining("\n"));
        String seatsAvailable = this.getSeats().stream().map(row -> row+"\n").collect(Collectors.joining(""));
        return String.format("Train Name: %s\nTrain ID: %s\nTrain No: %s \n\nStation Timings: \n%s\n\nSeats Available: \n%s" , trainName, trainId, trainNo, trainTimings, seatsAvailable);
    }

    // Getters
    public String getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public Map<String, String> getStationTimes() {
        return stationTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    // Setters
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}
