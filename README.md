## Required Libraries
Make sure you have the following JAR files added to your project (e.g., in IntelliJ or your `lib` folder):

1. **MySQL Connector JAR** (v9.4.0) – for database connectivity  
2. **JBCrypt** – for hashing passwords securely  
3. **iTextPDF JAR** – for generating boarding passes  

---

## Sample Database Setup

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS airline_db;
USE airline_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Flights table
CREATE TABLE IF NOT EXISTS flights (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(20) UNIQUE NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    available_seats INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    flight_id INT NOT NULL,
    number_of_seats INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(user_id),
    FOREIGN KEY(flight_id) REFERENCES flights(flight_id)
);

-- Add extra columns
ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'user';
ALTER TABLE flights ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ON_TIME';

-- Insert Admin User (password: admin@123)
INSERT INTO users (username, password, full_name, email, role)
VALUES (
    'admin', 
    '$2a$10$wE3o3dLjN9ZfZj2eqZt/4eE7R1ZcVJg5oGssN3a9Qafpc5D/3uV5W', 
    'Administrator', 
    'admin@example.com', 
    'admin'
);

-- Insert Sample Flights
INSERT INTO flights (flight_number, source, destination, departure_time, arrival_time, available_seats, price)
VALUES 
('AI101','Delhi','Mumbai','2024-12-25 08:00:00','2024-12-25 10:30:00',150,5500.00),
('AI102','Mumbai','Delhi','2024-12-25 12:00:00','2024-12-25 14:30:00',150,5500.00);
