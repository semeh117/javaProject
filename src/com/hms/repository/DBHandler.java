package com.hms.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Database Handler Class
 * Manages SQLite database connection and initialization
 */
public class DBHandler {
	private static final String DB_URL = "jdbc:sqlite:hms_database.db";
    private static DBHandler instance;
    private Connection connection;

	private  DBHandler() {
		initializeDatabase();
	}

    /**
     * Get singleton instance of DBHandler
     * @return DBHandler instance
     */
    public static synchronized DBHandler getInstance() {
        if (instance == null) {
            instance = new DBHandler();
        }
        return instance;
    }

    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                // Enable foreign keys
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
            } catch (SQLException e) {
                throw new SQLException("Failed to establish database connection: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Initialize database and create tables if they don't exist
     */

	private void initializeDatabase() {
		try (Connection conn = DriverManager.getConnection(DB_URL);
	             Statement stmt = conn.createStatement()) {
	            
	            // Enable foreign keys
	            stmt.execute("PRAGMA foreign_keys = ON");
	            
	            // Create tables directly (more reliable than reading from file)
	            createTablesDirectly(stmt);
	            
	        } catch (SQLException e) {
	            System.err.println("Error initializing database: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Create tables directly if schema file is not available
	     */
	    private void createTablesDirectly(Statement stmt) throws SQLException {
	        // Create User table
	        stmt.execute("CREATE TABLE IF NOT EXISTS User (" +
	                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                "username VARCHAR(50) NOT NULL UNIQUE, " +
	                "password VARCHAR(255) NOT NULL, " +
	                "full_name VARCHAR(100) NOT NULL, " +
	                "role VARCHAR(20) NOT NULL CHECK(role IN ('ADMIN', 'DOCTOR', 'NURSE')), " +
	                "email VARCHAR(100), " +
	                "phone VARCHAR(20), " +
	                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

	        // Create Patient table
	        stmt.execute("CREATE TABLE IF NOT EXISTS Patient (" +
	                "patient_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                "first_name VARCHAR(50) NOT NULL, " +
	                "last_name VARCHAR(50) NOT NULL, " +
	                "date_of_birth DATE NOT NULL, " +
	                "gender VARCHAR(10) NOT NULL CHECK(gender IN ('MALE', 'FEMALE', 'OTHER')), " +
	                "phone VARCHAR(20), " +
	                "email VARCHAR(100), " +
	                "address TEXT, " +
	                "emergency_contact VARCHAR(100), " +
	                "emergency_phone VARCHAR(20), " +
	                "blood_type VARCHAR(5), " +
	                "registration_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
	                "created_by INTEGER, " +
	                "FOREIGN KEY (created_by) REFERENCES User(user_id))");

	        // Create Diagnosis table
	        stmt.execute("CREATE TABLE IF NOT EXISTS Diagnosis (" +
	                "diagnosis_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                "patient_id INTEGER NOT NULL, " +
	                "doctor_id INTEGER NOT NULL, " +
	                "diagnosis_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
	                "symptoms TEXT, " +
	                "diagnosis_description TEXT NOT NULL, " +
	                "prescribed_medication TEXT, " +
	                "notes TEXT, " +
	                "status VARCHAR(20) DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'RESOLVED', 'FOLLOW_UP')), " +
	                "FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE, " +
	                "FOREIGN KEY (doctor_id) REFERENCES User(user_id))");

	        // Insert default admin user
	        stmt.execute("INSERT OR IGNORE INTO User (username, password, full_name, role, email) " +
	                "VALUES ('admin', 'admin123', 'System Administrator', 'ADMIN', 'admin@hms.com')");

	        System.out.println("Tables created successfully.");
	    }
		
	}

