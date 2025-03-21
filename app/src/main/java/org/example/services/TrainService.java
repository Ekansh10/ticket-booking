package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TrainService {
    private List<Train> trainList;
    ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String TRAIN_DB_PATH = "app/src/main/java/org/example/localDb/trains.json";

    // CONSTRUCTORS
    public TrainService() throws IOException{
        trainList = loadTrains();
    }



    //METHODS

    // Saving TrainList
    private void saveTrainList() throws IOException {
        File trainsfile = new File(TRAIN_DB_PATH);
        objectMapper.writeValue(trainsfile, trainList);
    }


    // Loading Trains
    public List<Train> loadTrains() throws IOException {
        File trains = new File(TRAIN_DB_PATH);
        return objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    // Searching Train
    public List<Train> searchTrains(String source, String destination){
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    // Valid Train
    private boolean validTrain(Train train, String source, String destination){
        List<String> stationOrder = train.getStations();

        int sourceIdx = stationOrder.indexOf(source.toLowerCase());
        int destinationIdx = stationOrder.indexOf(destination.toLowerCase());

        return (sourceIdx != -1 && destinationIdx != -1) && (sourceIdx < destinationIdx);
    }

    public AtomicReference<Train> getTrain(String tno){
        Optional<Train> tOpt = trainList.stream().filter(train -> train.getTrainNo().equals(tno)).findFirst();
        AtomicReference<Train> foundTrain = new AtomicReference<>();
        tOpt.ifPresent(foundTrain::set);
        return foundTrain;
    }

    public AtomicBoolean seatAvailable(AtomicReference<Train> t, int row, int col) throws IOException {

        if (t.get() == null) {
            return new AtomicBoolean(false); // Safe return instead of null
        }

        Train train = t.get();
        List<List<Integer>> seats = train.getSeats();

        if (row < 0 || row >= seats.size() || col < 0 || col >= seats.get(row).size()) {
            return new AtomicBoolean(false);
        }

        if (seats.get(row).get(col) == 0) {
            seats.get(row).set(col, 1);
            train.setSeats(seats);
            saveTrainList();
            return new AtomicBoolean(true);
        }

        return new AtomicBoolean(false);
    }


    public void update() throws IOException {
        saveTrainList();
    }
}
