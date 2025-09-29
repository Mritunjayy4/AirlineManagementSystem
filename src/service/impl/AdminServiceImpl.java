package service.impl;

import util.DBConnection;
import java.sql.*;
import java.util.Scanner;

public class AdminServiceImpl {

    /** Add a new flight */
    public void addFlight(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter flight number: ");
            String flightNumber = scanner.nextLine();

            System.out.print("Enter source: ");
            String source = scanner.nextLine();

            System.out.print("Enter destination: ");
            String destination = scanner.nextLine();

            System.out.print("Enter departure time (YYYY-MM-DD HH:MM): ");
            String departure = scanner.nextLine();

            System.out.print("Enter arrival time (YYYY-MM-DD HH:MM): ");
            String arrival = scanner.nextLine();

            System.out.print("Enter available seats: ");
            int seats = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter flight status (ON_TIME / DELAYED / CANCELLED): ");
            String status = scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO flights(flight_number, source, destination, departure_time, arrival_time, available_seats, price, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, flightNumber);
            ps.setString(2, source);
            ps.setString(3, destination);
            ps.setString(4, departure);
            ps.setString(5, arrival);
            ps.setInt(6, seats);
            ps.setDouble(7, price);
            ps.setString(8, status);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Flight added successfully!");
            } else {
                System.out.println("Failed to add flight.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding flight: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Update flight status */
    public void updateFlightStatus(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter flight number to update: ");
            String flightNumber = scanner.nextLine();

            System.out.print("Enter new status (ON_TIME / DELAYED / CANCELLED): ");
            String status = scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE flights SET status=? WHERE flight_number=?"
            );
            ps.setString(1, status);
            ps.setString(2, flightNumber);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Flight status updated successfully!");
            } else {
                System.out.println("Flight not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating flight status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Adjust available seats */
    public void adjustSeats(Scanner scanner) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter flight number: ");
            String flightNumber = scanner.nextLine();

            System.out.print("Enter new available seats: ");
            int seats = Integer.parseInt(scanner.nextLine());

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE flights SET available_seats=? WHERE flight_number=?"
            );
            ps.setInt(1, seats);
            ps.setString(2, flightNumber);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Available seats updated successfully!");
            } else {
                System.out.println("Flight not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error adjusting seats: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
