package com.hms.domain;

import java.time.LocalDateTime;

/**
 * Diagnosis Model Class
 * Represents a medical diagnosis/record for a patient
 */


public class Diagnosis {

    private int diagnosisId;
    private int patientId;
    private int doctorId;
    private LocalDateTime diagnosisDate;
    private String symptoms;
    private String diagnosisDescription;
    private String prescribedMedication;
    private String notes;
    private Status status;

    // Enum for diagnosis status
    public enum Status {
        ACTIVE, RESOLVED, FOLLOW_UP
    }

    // Constructors
    public Diagnosis() {
    }

    public Diagnosis(int patientId, int doctorId, String diagnosisDescription) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosisDescription = diagnosisDescription;
        this.status = Status.ACTIVE;
        this.diagnosisDate = LocalDateTime.now();
    }

    public Diagnosis(int diagnosisId, int patientId, int doctorId, LocalDateTime diagnosisDate,
                     String symptoms, String diagnosisDescription, String prescribedMedication,
                     String notes, Status status) {
        this.diagnosisId = diagnosisId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosisDate = diagnosisDate;
        this.symptoms = symptoms;
        this.diagnosisDescription = diagnosisDescription;
        this.prescribedMedication = prescribedMedication;
        this.notes = notes;
        this.status = status;
    }

	public int getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(int diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public LocalDateTime getDiagnosisDate() {
		return diagnosisDate;
	}

	public void setDiagnosisDate(LocalDateTime diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getDiagnosisDescription() {
		return diagnosisDescription;
	}

	public void setDiagnosisDescription(String diagnosisDescription) {
		this.diagnosisDescription = diagnosisDescription;
	}

	public String getPrescribedMedication() {
		return prescribedMedication;
	}

	public void setPrescribedMedication(String prescribedMedication) {
		this.prescribedMedication = prescribedMedication;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Diagnosis [diagnosisId=" + diagnosisId + ", patientId=" + patientId + ", doctorId=" + doctorId
				+ ", diagnosisDate=" + diagnosisDate + ", symptoms=" + symptoms + ", diagnosisDescription="
				+ diagnosisDescription + ", prescribedMedication=" + prescribedMedication + ", notes=" + notes
				+ ", status=" + status + "]";
	}
    
    

}

