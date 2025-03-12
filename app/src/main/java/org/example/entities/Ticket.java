package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {
    private static final String COUNTER_FILE = "app/src/main/java/org/example/localDb/counter.txt";
    private static final AtomicInteger counter;

    static {
        try {
            counter = new AtomicInteger(readLastCounter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonProperty("ticket_id")
    private String ticketId;
    @JsonProperty("user_id")
    private String userId;
    private String source;
    private String destination;
    @JsonProperty("date_of_booking")
    private Date dateOfBooking;
    @JsonProperty("date_of_travel")
    private Date dateOfTravel;
    private Train train;


    // Constructor
    public Ticket(){
    }

    public Ticket(String userId, String source, String destination, Date dateOfTravel, Train train) throws IOException {
        this.ticketId = generateTid();
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfBooking = new Date();
        this.dateOfTravel = dateOfTravel;
        this.train = train;
    }


    // Methods
    public String getTicketInfo(){
        return String.format("Ticket ID: %s belongs to User %s from %s to %s on %s", ticketId, userId, source, destination, dateOfTravel);
    }

    // Generate Random and Distinct Tid
    private static String generateTid() throws IOException {
        int id = counter.getAndIncrement();
        saveLastCounter(id);
        return "PNR-" + id;
    }

    private static int readLastCounter() throws IOException {
        File file = new File(COUNTER_FILE);
        if (!file.exists()){
            saveLastCounter(1001);
            return 1001;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            return Integer.parseInt(reader.readLine());
        }catch (IOException | NumberFormatException e){
            e.printStackTrace();
            return 1001;
        }
    }

    private static void saveLastCounter (int id) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))){
            writer.write(Integer.toString(id));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDateOfBooking() {
        return dateOfBooking;
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }


    // Setters
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateOfBooking(Date dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}
