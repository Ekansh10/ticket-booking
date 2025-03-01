package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {
    private User user; // Storing user at global level
    private List<User> userList; // List of users from localdb

    private static final String USER_PATH = "app/src/main/java/org/example/localDb/users.json";

    private ObjectMapper objectMapper = new ObjectMapper(); // Jackson's ObjectMapper to map values to classes


    // Constructor
    public UserBookingService(User u) throws IOException {
        this.user = u;
        File users = new File(USER_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    // Methods

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

    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USER_PATH);
        objectMapper.writeValue(usersFile, userList);
    }

}
