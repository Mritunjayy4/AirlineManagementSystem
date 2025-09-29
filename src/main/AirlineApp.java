package main;
import model.LoggedInUser;
import service.impl.*;

import java.util.Scanner;

public class AirlineApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserServiceImpl userService = new UserServiceImpl();
        ViewServiceImpl viewService = new ViewServiceImpl();
        BookingServiceImpl bookingService = new BookingServiceImpl();
        CancelServiceImpl cancelService = new CancelServiceImpl();
        AdminServiceImpl adminService = new AdminServiceImpl(); // new service for admin ops

        System.out.println("=== Welcome to Flight Management System ===");
        LoggedInUser currentUser = null;

        while (true) {
            if (currentUser == null) {
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
