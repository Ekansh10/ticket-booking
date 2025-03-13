/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.services.TrainService;
import org.example.services.UserBookingService;
import org.example.util.UserServiceUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.Date;


public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static String sourceToSearch;
    private static String destinationToSearch;

    public static void main(String[] args) throws IOException {
        System.out.println("Running Train Booking System!!");
        String option = "0";
        UserBookingService userBookingService;
        try{
            userBookingService = new UserBookingService();
        }catch (IOException ex){
            ex.printStackTrace();
            System.out.println("DB not Found!!");
            return;
        }
        boolean success;


        // APP LOOP
        while(!option.equals("8")){
            success = false;
            System.out.println("\n\nAvailable Options:");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. LogOut");
            System.out.println("4. Fetch Bookings");
            System.out.println("5. Search Trains");
            System.out.println("6. Book a Seat");
            System.out.println("7. Cancel my Booking");
            System.out.println("8. Exit App");
            System.out.println("Choose your option:");
            option = scanner.nextLine();
            option = option.strip();
            switch (option){
                case "1": // SIGN UP
                    if(userBookingService.getUser() != null){
                        System.out.println("Already Logged in!!\nLogOut First!!");
                    }else{

                        System.out.println("Enter the username: ");
                        String nameToSignup = scanner.nextLine();
                        System.out.println("Enter your password: ");
                        String passToSignUp = scanner.nextLine();

                        User u = new User();
                        u.setName(nameToSignup);
                        u.setPassword(passToSignUp);
                        u.setHashedPassword(UserServiceUtil.hashPassword(passToSignUp));
                        u.setUserId(UserServiceUtil.getUid());
                        u.setBookedTickets(new ArrayList<>());

                        success = userBookingService.signUp(u);
                        if(success){
                            System.out.println("Signed up Successfully!!");
                        }else{
                            System.out.println("Something Went Wrong!!!");
                        }
                    }
                    break;

                case "2": // LOGIN
                    if(userBookingService.getUser() == null){
                        System.out.println("Enter your username: ");
                        String nameToLogin = scanner.nextLine();
                        System.out.println("Enter your password: ");
                        String passToLogin = scanner.nextLine();

                        // Not a good Practice
                        User userToLogin = new User(nameToLogin, passToLogin,
                                UserServiceUtil.hashPassword(passToLogin),
                                new ArrayList<>(), UUID.randomUUID().toString());
                        try{
                            userBookingService = new UserBookingService(userToLogin);// Not a good Practice
                            success = userBookingService.loginUser();
                            if(success){
                                System.out.println("Login Successfull!!");
                            }else{
                                System.out.println("Invalid Credentials!!");
                            }
                        }catch (IOException ex){
                            System.out.println("Something Went Wrong!!");
                            return;
                        }
                    }
                    else{
                        System.out.println("Already Logged in!!\nPlease LogOut First!!");
                    }

                    break;

                case "3": // LOGOUT
                    if(userBookingService.getUser() == null){
                        System.out.println("Already Logged Out!!");
                    }else{
                        userBookingService.setUser(null);
                        sourceToSearch = null;
                        destinationToSearch = null;
                        //System.gc();
                        System.out.println("Successfully Logged Out!!");
                    }
                    break;

                case "4": // FETCH BOOKINGS
                    if(userBookingService.getUser() != null) {
                        System.out.println("Fetching Your Bookings...");
                        userBookingService.fetchTickets();
                    }else{
                        System.out.println("Invalid Session!!\nPlease Login !!");
                    }
                    break;


                case "5": // SEARCH TRAINS
                    List<Train> x = findTrains();
                    break;


                case "6": // BOOK SEAT
                    if(userBookingService.getUser() != null){
                        List<Train> found = findTrains();
                        if(found.isEmpty()){
                            break;
                        }
                        boolean validTrainNo = false;
                        String bookingTrainNo = "";
                        while (!validTrainNo){
                            System.out.println("Enter the train No: ");
                            bookingTrainNo = scanner.nextLine();
                            for(Train t : found){
                                if (t.getTrainNo().equals(bookingTrainNo)) {
                                    validTrainNo = true;
                                    break;
                                }
                            }
                            if(!validTrainNo){
                                System.out.println("Invalid Train Number !!");
                            }
                        }


                        Date dateOfTravel = null;
                        while(dateOfTravel == null){
                            try {
                                System.out.println("Enter Date of Travel (yyyy-MM-dd): ");
                                String inputDate = scanner.nextLine();
                                dateOfTravel = parseDate(inputDate);
                                System.out.println("Parsed Date: " + dateOfTravel);
                            } catch (ParseException e) {
                                System.out.println("Invalid date format! Please enter in yyyy-MM-dd format.");
                            }
                        }

                        userBookingService.seatBooking(bookingTrainNo, sourceToSearch, destinationToSearch, dateOfTravel, scanner);
                        scanner.nextLine(); // clearing scanner
                    }else{
                        System.out.println("Invalid Session!!\nPlease Login !!");
                    }
                    break;

                // need to fix the user context checking, maybe write a method in userserviceutil
                case "7": // CANCEL BOOKING
                    // Need to update the cancellation of booking in train.json as well
                    if(userBookingService.getUser() != null){
                        System.out.println("Enter your Ticket Id: ");
                        String tidToCancel = scanner.nextLine();
                        userBookingService.cancelBooking(tidToCancel);
                    }else{
                        System.out.println("Invalid Session!!\nPlease Login !!");
                    }

                    break;
                default:
                    System.out.println("Invalid Option");
            }

        }
    }

    // METHODS
    private static List<Train> findTrains() {
        System.out.println("Enter Source Station: ");
        sourceToSearch = scanner.nextLine();
        System.out.println("Enter Destination Station: ");
        destinationToSearch = scanner.nextLine();
        System.out.println("Searching Trains....");
        List<Train> searchedTrains = List.of();
        try{
            TrainService trainService = new TrainService();
            searchedTrains = trainService.searchTrains(sourceToSearch, destinationToSearch);

            if(searchedTrains.isEmpty()){
                System.out.println("No Trains for following route !!");
            }else{
                for(Train t : searchedTrains){
                    System.out.println("-------------------------------------------------");
                    System.out.println(t.getTrainInfo());
                    System.out.println("-------------------------------------------------");
                }
            }

        } catch (IOException ex) {
            System.out.println("Trains DB not Found!!");
        }
        return searchedTrains;
    }

    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);  // Prevents invalid dates like Feb 30
        return dateFormat.parse(dateStr);
    }
}
