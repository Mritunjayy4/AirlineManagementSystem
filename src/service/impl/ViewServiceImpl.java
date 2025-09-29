package service.impl;

import util.DBConnection;
import java.sql.*;
import java.util.Scanner;

public class ViewServiceImpl {

    public void viewFlights() {
        viewFlightsWithStatus();
    }

    public void viewFlightsWithStatus() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT flight_number, source, destination, departure_time, arrival_time, available_seats, price, status FROM flights";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("FlightNo | From → To | DepTime | ArrTime | Seats | Price | Status");
            while (rs.next()) {
                String fn = rs.getString("flight_number");
                String src = rs.getString("source");
                String dst = rs.getString("destination");
                Timestamp dt = rs.getTimestamp("departure_time");
                Timestamp at = rs.getTimestamp("arrival_time");
                int seats = rs.getInt("available_seats");
                double price = rs.getDouble("price");
                String status = rs.getString("status");
                System.out.printf("%-8s | %-8s→%-8s | %-16s | %-16s | %-5d | %-8.2f | %-9s\n",
                        fn, src, dst, dt.toString(), at.toString(), seats, price, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkFlightStatus(Scanner scanner) {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT flight_number, departure_time, status FROM flights WHERE flight_number = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, flightNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Flight " + rs.getString("flight_number") +
                        " departs at " + rs.getTimestamp("departure_time") +
                        ". Status: " + rs.getString("status"));
            } else {
                System.out.println("No flight found with number: " + flightNumber);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
