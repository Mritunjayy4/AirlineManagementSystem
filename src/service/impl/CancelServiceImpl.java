package service.impl;

import service.CancelService;
import util.DBConnection;
import java.sql.*;
import java.util.Scanner;

public class CancelServiceImpl implements CancelService {
    @Override
    public void cancelBooking(Scanner scanner, int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Enter Booking ID: ");
            int bookingId = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM bookings WHERE booking_id=? AND user_id=?");
            ps.setInt(1, bookingId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int flightId = rs.getInt("flight_id");
                int seats = rs.getInt("number_of_seats");
                double amount = rs.getDouble("total_amount");

                System.out.println("Refund Amount: ₹" + amount);
                System.out.print("Confirm cancel? (yes/no): ");
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("yes")) {
                    conn.setAutoCommit(false);

                    PreparedStatement cancel = conn.prepareStatement(
                            "UPDATE bookings SET status='CANCELLED' WHERE booking_id=?");
                    cancel.setInt(1, bookingId);
                    cancel.executeUpdate();

                    PreparedStatement updateSeats = conn.prepareStatement(
                            "UPDATE flights SET available_seats = available_seats + ? WHERE flight_id=?");
                    updateSeats.setInt(1, seats);
                    updateSeats.setInt(2, flightId);
                    updateSeats.executeUpdate();

                    conn.commit();
                    System.out.println("✓ Ticket Cancelled!");
                }
            } else {
                System.out.println("Booking not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}