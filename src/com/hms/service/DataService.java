package com.hms.service;

import com.hms.domain.*;
import com.hms.domain.User.Role;


import java.util.ArrayList;
import java.util.List;



public class DataService {
    private static DataService instance;
    private List<User> users;
    private List<Patient> patients;
    private List<Doctor> doctors;
    private List<Appointment> appointments;

    public DataService() {
        users = new ArrayList<>();
        patients = new ArrayList<>();
        doctors = new ArrayList<>();
        appointments = new ArrayList<>();
        
        // Seed data
        users.add(new User("semeh","123456","mechi",Role.ADMIN));
        users.add(new User("khalil", "111111","dali" ,Role.ADMIN));
        
        patients.add(new Patient());
        patients.add(new Patient());

        doctors.add(new Doctor("1", "Dr. Smith", "doctor@hospital.com", "Cardiology", "1112223333", "Mon-Fri 9-5"));
        doctors.add(new Doctor("2", "Dr. Jones", "jones@hospital.com", "Pediatrics", "4445556666", "Mon-Wed 9-5"));

        appointments.add(new Appointment("1", "1", "1", "John Doe", "Dr. Smith", "2023-10-20", "10:00", "Scheduled", "Routine Checkup"));
    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    /**
     * Simple in-memory authentication.
     * The identifier can be either username or email, and the password must match.
     */
    public User authenticate(String identifier, String password) {
        return users.stream()
                .filter(u ->
                        (u.getUsername() != null && u.getUsername().equalsIgnoreCase(identifier)) ||
                        (u.getEmail() != null && u.getEmail().equalsIgnoreCase(identifier)))
                .filter(u -> u.getPassword() != null && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<Patient> getPatients() { return patients; }
    public void addPatient(Patient patient) { patients.add(patient); }

    public List<Doctor> getDoctors() { return doctors; }
    public void addDoctor(Doctor doctor) { doctors.add(doctor); }

    public List<Appointment> getAppointments() { return appointments; }
    public void addAppointment(Appointment appointment) { appointments.add(appointment); }
    
    public DashboardStats getStats() {
        // Mock stats
        return new DashboardStats(patients.size(), doctors.size(), appointments.size(), 15000);
    }
    
    // Inner class for stats if not defined elsewhere
    public static class DashboardStats {
        public int totalPatients;
        public int totalDoctors;
        public int activeAppointments;
        public double revenue;

        public DashboardStats(int totalPatients, int totalDoctors, int activeAppointments, double revenue) {
            this.totalPatients = totalPatients;
            this.totalDoctors = totalDoctors;
            this.activeAppointments = activeAppointments;
            this.revenue = revenue;
        }
    }
}
