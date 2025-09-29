package service.impl;

import service.CancelService;
import util.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class CancelServiceImpl implements CancelService {

    @Override
    public void cancelBooking(Scanner scanner, int userId) {
        System.out.print("Enter Flight Number: ");
        String flightNumber = scanner.nextLine().trim();

        try (Connection conn = DBConnection.getConnection()) {
            // Find the booking for this user and flight
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT b.booking_id, b.number_of_seats, b.total_amount, b.flight_id " +
                            "FROM bookings b JOIN flights f ON b.flight_id = f.flight_id " +
                            "WHERE b.user_id=? AND f.flight_number=? AND b.status != 'CANCELLED'"
            );
            ps.setInt(1, userId);
            ps.setString(2, flightNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int seats = rs.getInt("number_of_seats");
                double amount = rs.getDouble("total_amount");
                int flightId = rs.getInt("flight_id");

                System.out.println("Refund Amount: ₹" + amount);
                System.out.print("Confirm cancel? (yes/no): ");
                String confirm = scanner.nextLine().trim();

                if (confirm.equalsIgnoreCase("yes")) {
                    conn.setAutoCommit(false);

                    // Cancel the booking
                    PreparedStatement cancel = conn.prepareStatement(
                            "UPDATE bookings SET status='CANCELLED' WHERE booking_id=?"
                    );
                    cancel.setInt(1, bookingId);
                    cancel.executeUpdate();

                    // Update flight seats
                    PreparedStatement updateSeats = conn.prepareStatement(
                            "UPDATE flights SET available_seats = available_seats + ? WHERE flight_id=?"
                    );
                    updateSeats.setInt(1, seats);
                    updateSeats.setInt(2, flightId);
                    updateSeats.executeUpdate();

                    conn.commit();
                    System.out.println("✓ Ticket Cancelled!");
                } else {
                    System.out.println("Cancellation aborted.");
                }
            } else {
                System.out.println("No active booking found for this flight!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Rolling back transaction...");
                DBConnection.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
