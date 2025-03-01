package org.example.entities;
import java.util.List;

public class User {
    private String name;
    private String password;
    private String hashedPassword;
    private List<Ticket> bookedTickets;
    private String userId;

    // Constructors
    public User(){};
    public User(String name, String password, String hashedPassword, List<Ticket> bookedTickets, String userID){
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.bookedTickets = bookedTickets;
        this.userId = userID;
    }

    // Methods
    public void printTickets(){
        this.bookedTickets.forEach((x) -> System.out.println(x.getTicketInfo()));
    }



    // Getters
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public String getHashedPassword(){
        return hashedPassword;
    }
    public List<Ticket> getBookedTickets(){
        return bookedTickets;
    }
    public String getUserId(){
        return userId;
    }

    // Setters
    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setHashedPassword(String hashedPassword){
        this.hashedPassword = hashedPassword;
    }
    public void setBookedTickets(List<Ticket> bookedTickets){
        this.bookedTickets = bookedTickets;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
}
