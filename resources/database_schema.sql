-- Hospital Management System Database Schema
-- SQLite Database Schema

-- User Table: Stores system users (doctors, nurses, administrators)
CREATE TABLE IF NOT EXISTS User (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK(role IN ('ADMIN', 'DOCTOR', 'NURSE')),
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Patient Table: Stores patient information
CREATE TABLE IF NOT EXISTS Patient (
    patient_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK(gender IN ('MALE', 'FEMALE', 'OTHER')),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    blood_type VARCHAR(5),
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    FOREIGN KEY (created_by) REFERENCES User(user_id)
);

-- Diagnosis Table: Stores patient diagnoses and medical records
CREATE TABLE IF NOT EXISTS Diagnosis (
    diagnosis_id INTEGER PRIMARY KEY AUTOINCREMENT,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    diagnosis_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    symptoms TEXT,
    diagnosis_description TEXT NOT NULL,
    prescribed_medication TEXT,
    notes TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'RESOLVED', 'FOLLOW_UP')),
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES User(user_id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_patient_name ON Patient(last_name, first_name);
CREATE INDEX IF NOT EXISTS idx_diagnosis_patient ON Diagnosis(patient_id);
CREATE INDEX IF NOT EXISTS idx_diagnosis_doctor ON Diagnosis(doctor_id);
CREATE INDEX IF NOT EXISTS idx_user_role ON User(role);

-- Insert default admin user (password: admin123 - should be hashed in production)
INSERT OR IGNORE INTO User (username, password, full_name, role, email) 
VALUES ('admin', 'admin123', 'System Administrator', 'ADMIN', 'admin@hms.com');

