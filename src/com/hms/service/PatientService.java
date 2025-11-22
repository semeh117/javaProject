package com.hms.service;

import com.hms.exception.DatabaseException;
import com.hms.exception.ValidationException;
import com.hms.domain.Patient;
import com.hms.repository.PatientDAO;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PatientService {
	private PatientDAO patientDAO;
    private Map<Integer, Patient> patientCache; // Map for quick patient lookup by ID
    private List<Patient> patientList; // List to store all patients
    private Set<Patient> uniquePatients; // Set to ensure unique patients
    
    public PatientService() {
        this.patientDAO = new PatientDAO();
        this.patientCache = new HashMap<>();
        this.patientList = new ArrayList<>();
        this.uniquePatients = new HashSet<>();
        loadPatients();
    }
    
    /**
     * Load patients from database into memory
     */
    private void loadPatients() {
        try {
            patientList = patientDAO.getAllPatients();
            patientCache.clear();
            uniquePatients.clear();
            for (Patient patient : patientList) {
                patientCache.put(patient.getPatientId(), patient);
                uniquePatients.add(patient);
            }
        } catch (DatabaseException e) {
            System.err.println("Error loading patients: " + e.getMessage());
            patientList = new ArrayList<>();
        }
    }
    
    /**
     * Get all patients
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientList);
    }
    
    /**
     * Add a new patient
     * @param patient Patient to add
     * @return Generated patient ID
     * @throws DatabaseException if database operation fails
     * @throws ValidationException if validation fails
     */
    public int addPatient(Patient patient) throws DatabaseException, ValidationException {
        int patientId = patientDAO.insertPatient(patient);
        patient.setPatientId(patientId);
        patientList.add(patient);
        patientCache.put(patientId, patient);
        uniquePatients.add(patient);
        return patientId;
    }
    
    /**
     * Get patients with blood type using Stream and Optional
     * @param bloodType Blood type to filter
     * @return List of patients with specified blood type
     */
    public List<Patient> getPatientsByBloodType(String bloodType) {
        return patientList.stream()
                .filter(patient -> 
                    Optional.ofNullable(patient.getBloodType())
                            .map(bt -> bt.equalsIgnoreCase(bloodType))
                            .orElse(false)
                )
                .collect(Collectors.toList());
    }

    /**
     * Update patient information
     * @param patient Updated patient object
     * @return true if update successful
     * @throws DatabaseException if database operation fails
     * @throws ValidationException if validation fails
     */
    public boolean updatePatient(Patient patient) throws DatabaseException, ValidationException {
        boolean success = patientDAO.updatePatient(patient);
        if (success) {
            // Update in-memory structures
            patientCache.put(patient.getPatientId(), patient);
            // Update in list
            patientList.replaceAll(p -> 
                p.getPatientId() == patient.getPatientId() ? patient : p
            );
            uniquePatients.removeIf(p -> p.getPatientId() == patient.getPatientId());
            uniquePatients.add(patient);
        }
        return success;
    }

    /**
     * Delete patient
     * @param patientId Patient ID to delete
     * @return true if deletion successful
     * @throws DatabaseException if database operation fails
     */
    public boolean deletePatient(int patientId) throws DatabaseException {
        boolean success = patientDAO.deletePatient(patientId);
        if (success) {
            // Remove from in-memory structures
            patientCache.remove(patientId);
            patientList.removeIf(p -> p.getPatientId() == patientId);
            uniquePatients.removeIf(p -> p.getPatientId() == patientId);
        }
        return success;
    }

    /**
     * Refresh patient data from database
     */
    public void refreshPatients() {
        loadPatients();
    }
    
    /**
     * Search patients by name
     * @param searchTerm Search term
     * @return List of matching patients
     */
    public List<Patient> searchPatientsByName(String searchTerm) {
        String lowerSearchTerm = searchTerm.toLowerCase();
        return patientList.stream()
                .filter(patient -> 
                    patient.getFirstName().toLowerCase().contains(lowerSearchTerm) ||
                    patient.getLastName().toLowerCase().contains(lowerSearchTerm) ||
                    patient.getFullName().toLowerCase().contains(lowerSearchTerm)
                )
                .collect(Collectors.toList());
    }
    
    /**
     * Filter patients by age range
     * @param minAge Minimum age
     * @param maxAge Maximum age
     * @return List of patients in age range
     */
    public List<Patient> filterPatientsByAge(int minAge, int maxAge) {
        return patientList.stream()
                .filter(patient -> {
                    int age = patient.getAge();
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());
    }

	/**
     * Get patient statistics using Streams
     * @return Map with various statistics
     */
    public Map<String, Object> getPatientStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", patientList.size());
        stats.put("averageAge", getAverageAge());
        stats.put("ageGroups", getPatientCountByAgeGroup());
        stats.put("genderDistribution", groupPatientsByGender().entrySet().stream()
                .collect(Collectors.toMap(
                    e -> e.getKey().name(),
                    e -> e.getValue().size()
                )));
        return stats;
    }
    
    /**
     * Calculate average age of patients
     * @return Average age
     */
    private double getAverageAge() {
        if (patientList.isEmpty()) {
            return 0.0;
        }
        return patientList.stream()
                .mapToInt(Patient::getAge)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Get patient count by age group
     * @return Map with age group counts
     */
    private Map<String, Long> getPatientCountByAgeGroup() {
        Map<String, Long> ageGroups = new HashMap<>();
        ageGroups.put("0-17", patientList.stream().filter(p -> p.getAge() < 18).count());
        ageGroups.put("18-64", patientList.stream().filter(p -> p.getAge() >= 18 && p.getAge() < 65).count());
        ageGroups.put("65+", patientList.stream().filter(p -> p.getAge() >= 65).count());
        return ageGroups;
    }
    
    /**
     * Group patients by gender
     * @return Map with gender as key and list of patients as value
     */
    private Map<Patient.Gender, List<Patient>> groupPatientsByGender() {
        return patientList.stream()
                .collect(Collectors.groupingBy(Patient::getGender));
    }
    
}

