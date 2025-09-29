package service.impl;

import service.BookingService;
import util.DBConnection;
import util.BoardingPassGenerator;

import java.sql.*;
import java.util.Scanner;

public class BookingServiceImpl implements BookingService {
    @Override
    public void bookTicket(Scanner scanner, int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter flight number: ");
            String flightNumber = scanner.next();

            System.out.print("Enter seats to book: ");
            int seats = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // 🔹 Lookup flight using flight_number
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM flights WHERE flight_number=?");
            ps.setString(1, flightNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int flightId = rs.getInt("flight_id");
                int available = rs.getInt("available_seats");
                double price = rs.getDouble("price");

                String source = rs.getString("source");
                String destination = rs.getString("destination");
                Timestamp departureTime = rs.getTimestamp("departure_time");
                Timestamp arrivalTime = rs.getTimestamp("arrival_time");

                if (available >= seats) {
                    double total = price * seats;
                    System.out.println("Total Amount: ₹" + total);
                    System.out.print("Confirm booking? (yes/no): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("yes")) {
                        try {
                            conn.setAutoCommit(false);

                            // 🔹 Update available seats
                            PreparedStatement updateSeats = conn.prepareStatement(
                                    "UPDATE flights SET available_seats=? WHERE flight_id=?");
                            updateSeats.setInt(1, available - seats);
                            updateSeats.setInt(2, flightId);
                            updateSeats.executeUpdate();

                            // 🔹 Insert booking record
                            PreparedStatement insert = conn.prepareStatement(
                                    "INSERT INTO bookings(user_id, flight_id, number_of_seats, total_amount, status) VALUES (?,?,?,?,?)");
                            insert.setInt(1, userId);
                            insert.setInt(2, flightId);
                            insert.setInt(3, seats);
                            insert.setDouble(4, total);
                            insert.setString(5, "CONFIRMED");
                            insert.executeUpdate();

                            conn.commit();
                            System.out.println("✓ Booking Successful!");

                            // 🔹 Fetch passenger name from users table
                            String passengerName = "Unknown";
                            PreparedStatement userPs = conn.prepareStatement("SELECT full_name FROM users WHERE user_id=?");
                            userPs.setInt(1, userId);
                            ResultSet userRs = userPs.executeQuery();
                            if (userRs.next()) {
                                passengerName = userRs.getString("full_name");
                            }

                            // 🔹 Generate boarding pass PDF
                            String fileName = "BoardingPass_" + userId + "_" + flightNumber + ".pdf";
                            BoardingPassGenerator.generateBoardingPass(
                                    fileName,
                                    passengerName,           // ✅ real full name
                                    flightNumber,
                                    source,
                                    destination,
                                    departureTime.toString(),
                                    arrivalTime.toString(),
                                    seats,
                                    total
                            );

                        } catch (Exception e) {
                            conn.rollback();
                            System.out.println("Booking failed, transaction rolled back.");
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Booking cancelled.");
                    }
                } else {
                    System.out.println("Only " + available + " seats available!");
                }
            } else {
                System.out.println("Invalid Flight Number!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
