package com.hms.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;


public class Patient {
    private int patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
    private String bloodType;
    private LocalDateTime registrationDate;
    private Integer createdBy;

    // Enum for gender
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public Patient() {
    }
    
    public Patient(String firstName, String lastName, LocalDate dateOfBirth, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public Patient(int patientId, String firstName, String lastName, LocalDate dateOfBirth,
                   Gender gender, String phone, String email, String address,
                   String emergencyContact, String emergencyPhone, String bloodType,
                   LocalDateTime registrationDate, Integer createdBy) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.bloodType = bloodType;
        this.registrationDate = registrationDate;
        this.createdBy = createdBy;
    }

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
    /**
     * Calculate and return the age of the patient
     * @return age in years
     */
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Get full name of the patient
     * @return full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

	@Override
	public String toString() {
		return "Patient [patientId=" + patientId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", phone=" + phone + ", email=" + email
				+ ", address=" + address + ", emergencyContact=" + emergencyContact + ", emergencyPhone="
				+ emergencyPhone + ", bloodType=" + bloodType + ", registrationDate=" + registrationDate
				+ ", createdBy=" + createdBy + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(patientId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		return patientId == other.patientId;
	}
    
}

