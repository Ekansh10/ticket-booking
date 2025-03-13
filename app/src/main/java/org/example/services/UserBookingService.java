package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UserBookingService {
    private final TrainService ts = new TrainService();
    private User user; // Storing user at global level
    private List<User> userList; // List of users from localdb

    private static final String USER_PATH = "app/src/main/java/org/example/localDb/users.json";

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT); // Jackson's ObjectMapper to map values to classes


    // Constructor
    public UserBookingService(User u) throws IOException {
        this.user = u;
        userList = loadUsers();
    }

    public UserBookingService() throws IOException {
        userList = loadUsers();
    }
    // Methods

    // Loading Users
    public List<User> loadUsers() throws IOException{
        System.out.println("Before");
        File users = new File(USER_PATH);
        System.out.println("After");
        System.out.println(users.toPath());
        System.out.println("File exists: " + users.exists());
        System.out.println("File readable: " + users.canRead());
        System.out.println("File size: " + users.length() + " bytes");

        return objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    // LOGIN
    // Not a good Practice
    public boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter( user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
                }).findFirst();
        foundUser.ifPresent(value -> this.user = value);
        return foundUser.isPresent();
    }

    // SIGNUP
    public boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    // Updating UserList
    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USER_PATH);
        objectMapper.writeValue(usersFile, userList);
    }


    // Fetch Ticket details
    public void fetchTickets(){
        if(this.user == null){
            System.out.println("Invalid Session!! Please Login First!!");
        }else{
            this.user.printTickets();
        }
    }


    // Book Seat
    public void seatBooking(String tNo, String source, String destination, Date dot, Scanner scanner) throws IOException {

        AtomicReference<Train> choosenTrain = ts.getTrain(tNo);
        System.out.println("\nChoose your seat:\n");
        for(List<Integer> row : choosenTrain.get().getSeats()) {
            System.out.println(row);
        }
        System.out.println("\n!! Seats having 1 are booked !!\n");

        System.out.println("\nEnter the Row:");
        int row = scanner.nextInt();
        System.out.println("\nEnter the Column:");
        int col = scanner.nextInt();
        row = row -1;
        col = col -1;
        AtomicBoolean isAvailable = ts.seatAvailable(choosenTrain, row, col);

        if(isAvailable.get()){
            Ticket t = new Ticket(this.user.getUserId(), source, destination, dot, choosenTrain.get(), row, col);
//            System.out.println("Before ticket");
//            System.out.println(t.getTicketInfo());
//            System.out.println("After ticket");
//            System.out.println(this.user.getBookedTickets());
//            this.user.getBookedTickets().add(t);
//            System.out.println(this.user.getBookedTickets());
//            User newUser = this.user;
//            newUser.getBookedTickets().add(t);
//            userList.add(newUser);
//            saveUserListToFile();

            // Due to new user object assignment to UserBookingService at the time of login, a new uid was created to declare the user and thus it was giving errors
//            System.out.println("\nLOOP BEGINS");
//            for (User u : userList) {
//                System.out.println(u.getUserId());
//                System.out.println(this.user.getUserId());
//                if (u.getUserId().equals(this.user.getUserId())) {
//                    System.out.println("Got it");
//                    u.getBookedTickets().add(t);
//                    break;
//                }
//            }
//            System.out.println("\nLOOP ENDS");

            this.user.getBookedTickets().add(t); // Now it'll work
            saveUserListToFile();

            System.out.println("Ticket Booked Successfully !!");
            System.out.println("\nYour Booked Tickets:\n");
            user.printTickets();

        }else{
            System.out.println("The Seat is NOT Available !!");
        }

    }




    // Cancel Booking
    public boolean cancelBooking(String ticketId) throws IOException {
        if(ticketId.isEmpty()){
            System.out.println("Invalid Ticket ID !!");
            return Boolean.FALSE;
        }
        Optional<Ticket> t = user.getBookedTickets().stream().filter(ticket -> ticket.getTicketId().equals(ticketId)).findFirst();

        if(t.isPresent()){
            AtomicReference<Train> train = ts.getTrain(t.get().getTrain().getTrainNo());
            List<List<Integer>> cancelledSeats = train.get().getSeats();
            cancelledSeats.get(t.get().getRow()).set(t.get().getCol(), 0);
            train.get().setSeats(cancelledSeats);
            ts.update();

            user.getBookedTickets().remove(t.get());
            saveUserListToFile();

            System.out.println("Ticket with ticket ID: " + ticketId +" has been canceled successfully !!");
            return Boolean.TRUE;
        }else{
            System.out.println("No Ticket found with ID: " + ticketId );
            return Boolean.FALSE;
        }
    }


    // Getters
    public User getUser() {
        return user;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }
}
