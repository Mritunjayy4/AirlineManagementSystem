package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class BoardingPassGenerator {

    public static void generateBoardingPass(String fileName, String passengerName,
                                            String flightNumber, String source, String destination,
                                            String departureTime, String arrivalTime, int seatsBooked,
                                            double totalAmount) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Boarding Pass", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // empty line

            // Passenger Details
            document.add(new Paragraph("Passenger Name: " + passengerName));
            document.add(new Paragraph("Flight Number: " + flightNumber));
            document.add(new Paragraph("From: " + source + " → To: " + destination));
            document.add(new Paragraph("Departure: " + departureTime));
            document.add(new Paragraph("Arrival: " + arrivalTime));
            document.add(new Paragraph("Seats Booked: " + seatsBooked));
            document.add(new Paragraph("Total Amount Paid: ₹" + totalAmount));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Have a safe journey!", new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)));

            document.close();
            System.out.println("✅ Boarding pass generated: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
