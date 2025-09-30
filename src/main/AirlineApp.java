package main;
import model.LoggedInUser;
import service.BookingService;
import service.CancelService;
import service.UserService;
import service.ViewService;
import service.impl.*;

import java.util.Scanner;

public class AirlineApp {
    //The Facade design pattern in Java provides a simplified interface to a complex subsystem.
    // It is a structural design pattern that aims to hide the complexities of a system from its clients,
    // making it easier to use and reducing dependencies.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserServiceImpl(); //login and register
        ViewService viewService = new ViewServiceImpl(); //view flight status and availiable flights
        BookingService bookingService = new BookingServiceImpl(); //Book tickets
        CancelService cancelService = new CancelServiceImpl(); //Cancel tickets
        AdminServiceImpl adminService = new AdminServiceImpl(); // Service for admin ops like add flight,change status

        System.out.println("=== Welcome to Flight Management System ===");
        LoggedInUser currentUser = null;//Represents currently logged in user and stores their role and ID.(name roll and id)

        while (true) {
            if (currentUser == null) { //no user logged in
                System.out.println("\n1. Register\n2. Login\n3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    userService.register(scanner);
                } else if (choice == 2) {
                    currentUser = userService.login(scanner);
                } else if (choice == 3) {
                    break;
                }
            } else {
                if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                    // Admin menu
                    System.out.println("\n=== Admin Menu ===");
                    System.out.println("1. Add Flight");
                    System.out.println("2. Update Flight Status");
                    System.out.println("3. Adjust Seats");
                    System.out.println("4. View Flights");
                    System.out.println("5. Logout");

                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1 -> adminService.addFlight(scanner);
                        case 2 -> adminService.updateFlightStatus(scanner);
                        case 3 -> adminService.adjustSeats(scanner);
                        case 4 -> viewService.viewFlights();
                        case 5 -> currentUser = null;
                        default -> System.out.println("Invalid choice, try again.");
                    }
                } else {
                    // Normal user menu
                    System.out.println("\n1. View Flights");
                    System.out.println("2. Book Ticket");
                    System.out.println("3. Cancel Ticket");
                    System.out.println("4. Check Flight Status");
                    System.out.println("5. Logout");

                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1 -> viewService.viewFlights();
                        case 2 -> bookingService.bookTicket(scanner, currentUser.getUserId());
                        case 3 -> cancelService.cancelBooking(scanner, currentUser.getUserId());
                        case 4 -> viewService.checkFlightStatus(scanner);
                        case 5 -> currentUser = null;
                        default -> System.out.println("Invalid choice, try again.");
                    }
                }
            }
        }
        System.out.println("Goodbye!");
    }
}
