package com.hms.repository;

import com.hms.exception.DatabaseException;
import com.hms.exception.ValidationException;
import com.hms.domain.Patient;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Patient operations
 * Demonstrates CRUD operations with SQLException handling
 */
public class PatientDAO {
	private DBHandler dbHandler;

	public PatientDAO() {
		this.dbHandler = DBHandler.getInstance();
	}
    /**
     * Insert a new patient into the database
     * Demonstrates SQLException handling
     * @param patient Patient object to insert
     * @return Generated patient ID
     * @throws DatabaseException if database operation fails
     * @throws ValidationException if validation fails
     */
    public int insertPatient(Patient patient) throws DatabaseException, ValidationException {
        // Validate patient data
        validatePatient(patient);

        String sql = "INSERT INTO Patient (first_name, last_name, date_of_birth, gender, " +
                     "phone, email, address, emergency_contact, emergency_phone, blood_type, " +
                     "registration_date, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            pstmt.setString(4, patient.getGender().name());
            pstmt.setString(5, patient.getPhone());
            pstmt.setString(6, patient.getEmail());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getEmergencyContact());
            pstmt.setString(9, patient.getEmergencyPhone());
            pstmt.setString(10, patient.getBloodType());
            
            LocalDateTime regDate = patient.getRegistrationDate();
            if (regDate == null) {
                regDate = LocalDateTime.now();
            }
            pstmt.setTimestamp(11, Timestamp.valueOf(regDate));
            
            if (patient.getCreatedBy() != null) {
                pstmt.setInt(12, patient.getCreatedBy());
            } else {
                pstmt.setNull(12, Types.INTEGER);
            }

            // Execute insert
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Inserting patient failed, no rows affected.");
            }

            // Get generated patient ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int patientId = generatedKeys.getInt(1);
                    patient.setPatientId(patientId);
                    return patientId;
                } else {
                    throw new DatabaseException("Inserting patient failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            // Handle SQLException with custom DatabaseException
            throw new DatabaseException("Error inserting patient: " + e.getMessage(), e);
        }
    }

    /**
     * Get patient by ID
     * @param patientId Patient ID
     * @return Patient object or null if not found
     * @throws DatabaseException if database operation fails
     */
    public Patient getPatientById(int patientId) throws DatabaseException {
        String sql = "SELECT * FROM Patient WHERE patient_id = ?";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving patient: " + e.getMessage(), e);
        }
    }

    /**
     * Get all patients
     * @return List of all patients
     * @throws DatabaseException if database operation fails
     */
    public List<Patient> getAllPatients() throws DatabaseException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient ORDER BY last_name, first_name";

        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all patients: " + e.getMessage(), e);
        }

        return patients;
    }

    /**
     * Update patient information
     * @param patient Patient object with updated information
     * @return true if update successful
     * @throws DatabaseException if database operation fails
     * @throws ValidationException if validation fails
     */
    public boolean updatePatient(Patient patient) throws DatabaseException, ValidationException {
        if (patient.getPatientId() == 0) {
            throw new ValidationException("Patient ID is required for update operation.");
        }

        validatePatient(patient);

        String sql = "UPDATE Patient SET first_name = ?, last_name = ?, date_of_birth = ?, " +
                     "gender = ?, phone = ?, email = ?, address = ?, emergency_contact = ?, " +
                     "emergency_phone = ?, blood_type = ? WHERE patient_id = ?";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            pstmt.setString(4, patient.getGender().name());
            pstmt.setString(5, patient.getPhone());
            pstmt.setString(6, patient.getEmail());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getEmergencyContact());
            pstmt.setString(9, patient.getEmergencyPhone());
            pstmt.setString(10, patient.getBloodType());
            pstmt.setInt(11, patient.getPatientId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error updating patient: " + e.getMessage(), e);
        }
    }

    /**
     * Delete patient by ID
     * @param patientId Patient ID
     * @return true if deletion successful
     * @throws DatabaseException if database operation fails
     */
    public boolean deletePatient(int patientId) throws DatabaseException {
        String sql = "DELETE FROM Patient WHERE patient_id = ?";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting patient: " + e.getMessage(), e);
        }
    }

    /**
     * Map ResultSet to Patient object
     */
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }
        
        String genderStr = rs.getString("gender");
        if (genderStr != null) {
            patient.setGender(Patient.Gender.valueOf(genderStr));
        }
        
        patient.setPhone(rs.getString("phone"));
        patient.setEmail(rs.getString("email"));
        patient.setAddress(rs.getString("address"));
        patient.setEmergencyContact(rs.getString("emergency_contact"));
        patient.setEmergencyPhone(rs.getString("emergency_phone"));
        patient.setBloodType(rs.getString("blood_type"));
        
        Timestamp regDate = rs.getTimestamp("registration_date");
        if (regDate != null) {
            patient.setRegistrationDate(regDate.toLocalDateTime());
        }
        
        int createdBy = rs.getInt("created_by");
        if (!rs.wasNull()) {
            patient.setCreatedBy(createdBy);
        }
        
        return patient;
    }

    /**
     * Validate patient data
     * @param patient Patient to validate
     * @throws ValidationException if validation fails
     */
    private void validatePatient(Patient patient) throws ValidationException {
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required.");
        }
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required.");
        }
        if (patient.getDateOfBirth() == null) {
            throw new ValidationException("Date of birth is required.");
        }
        if (patient.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future.");
        }
        if (patient.getGender() == null) {
            throw new ValidationException("Gender is required.");
        }
    }

}

