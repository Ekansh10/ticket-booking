package org.example.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Date;

public class Train {
    private String trainId;
    private String trainName;
    private String trainNo;
    private List<List<Integer>> seats;
    private HashMap<String, Date> stationTimes;
    private List<String> stations;

}
