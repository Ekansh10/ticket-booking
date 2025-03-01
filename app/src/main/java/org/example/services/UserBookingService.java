package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserBookingService {
    private User user; // Storing user at global level
    private List<User> userList; // List of users from localdb

    private static final String USER_PATH = "app/src/main/java/org/example/localDb/users.json";

    private ObjectMapper objectMapper = new ObjectMapper(); // Jackson's ObjectMapper to map values to classes

    public UserBookingService(User u) throws IOException {
        this.user = u;
        File users = new File(USER_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }
}
