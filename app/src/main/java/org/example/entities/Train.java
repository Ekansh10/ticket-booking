package org.example.entities;

import java.util.*;

public class Train {
    private String trainId;
    private String trainName;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTimes;
    private List<String> stations;

    // Constructor
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
        return String.format("Train Name: %s, Train ID: %s, Train No: %s", trainName, trainId, trainNo);
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
