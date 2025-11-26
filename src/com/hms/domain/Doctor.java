package com.hms.domain;


public class Doctor {
    private String id;
    private String name;
    private String email;
    private String specialty;
    private String contact;
    private String availability;
    private String password;

	public Doctor(String id, String name, String email, String specialty, String contact, String availability) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialty = specialty;
        this.contact = contact;
        this.availability = availability;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
