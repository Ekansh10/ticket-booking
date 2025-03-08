package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {

    private User user; // Storing user at global level
    private List<User> userList; // List of users from localdb

    private static final String USER_PATH = "/home/ekansh-mahajan/Desktop/PROJECTS/IRCTC/app/src/main/java/org/example/localDb/users.json";

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
    public boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter( user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
                }).findFirst();
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
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }else{
            System.out.println("User Not Found!!");
        }
    }


    // Cancel Booking
    public boolean cancelBooking(String ticketId){
        if(ticketId.isEmpty()){
            System.out.println("Invalid Ticket ID !!");
            return Boolean.FALSE;
        }

        boolean removed = user.getBookedTickets().removeIf(Ticket -> Ticket.getTicketId().equals(ticketId));
        if(removed){
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
}
