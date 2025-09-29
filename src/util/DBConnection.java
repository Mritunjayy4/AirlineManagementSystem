package util;

import java.sql.Connection;
import java.sql.DriverManager;

/* CONNECTION STEPS:
 import package
 load and register
 create connection
 execute statement
 process the results
 */

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/airline_db"; //general format containing your database name
    private static final String USER = "root";
    private static final String PASSWORD = "12345678"; //MY CUSTOM PASSWORD

    public static Connection getConnection() {
        try { //TRY CATCH BECAUSE IT THROWS EXCETPION
            Class.forName("com.mysql.cj.jdbc.Driver"); //LOAD THE DRIVER
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace(); // Prints the name of Exception
            return null;
        }
    }
}