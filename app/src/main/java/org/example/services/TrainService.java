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

    public AtomicBoolean seatAvailable(AtomicReference<Train> t){

        AtomicBoolean present = new AtomicBoolean(false);

        if(t.get() == null){
            return null;
        }
        else{
            Train train = t.get();
            List<List<Integer>> seats = train.getSeats();
            for (List<Integer> seat : seats) {
                for (int j = 0; j < seat.size(); j++) {
                    if (seat.get(j) == 0) {
                        seat.set(j, 1);
                        present.set(true);
                        return present;
                    }
                }
            }
        }
        return present;
    }
}
