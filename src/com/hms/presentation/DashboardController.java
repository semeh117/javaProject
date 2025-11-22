package com.hms.presentation;

import com.hms.exception.DatabaseException;
import com.hms.exception.ValidationException;
import com.hms.domain.Patient;
import com.hms.domain.User;
import com.hms.service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DashboardController {
	@FXML
    private Label welcomeLabel;
    
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea addressArea;
    @FXML
    private TextField emergencyContactField;
    @FXML
    private TextField emergencyPhoneField;
    @FXML
    private TextField bloodTypeField;
    
    @FXML
    private Button addPatientButton;
    @FXML
    private Button updatePatientButton;
    @FXML
    private Button deletePatientButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterAgeComboBox;
    
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, Integer> idColumn;
    @FXML
    private TableColumn<Patient, String> firstNameColumn;
    @FXML
    private TableColumn<Patient, String> lastNameColumn;
    @FXML
    private TableColumn<Patient, Integer> ageColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> emailColumn;
    
    private PatientService patientService;
    private User currentUser;
    private ObservableList<Patient> patientObservableList;

    /**
     * Initialize controller
     * Demonstrates Lambda expressions for event handling
     */
    @FXML
    public void initialize() {
        patientService = new PatientService();
        patientObservableList = FXCollections.observableArrayList();
        
        // Initialize gender combo box
        genderComboBox.getItems().addAll("MALE", "FEMALE", "OTHER");
        
        // Initialize age filter combo box
        filterAgeComboBox.getItems().addAll("All", "0-17 (Child)", "18-64 (Adult)", "65+ (Senior)");
        filterAgeComboBox.setValue("All");
        
        // Setup table columns
        setupTableColumns();
        
        // Lambda expressions for button event handling
        addPatientButton.setOnAction(event -> handleAddPatient());
        updatePatientButton.setOnAction(event -> handleUpdatePatient());
        deletePatientButton.setOnAction(event -> handleDeletePatient());
        refreshButton.setOnAction(event -> handleRefresh());
        searchButton.setOnAction(event -> handleSearch());
        
        // Lambda expression for table selection
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadPatientToForm(newSelection);
            }
        });
        
        // Lambda expression for age filter
        filterAgeComboBox.setOnAction(event -> handleAgeFilter());
        
        // Load initial data
        loadPatients();
    }

    /**
     * Set current user and update welcome label
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (welcomeLabel != null && user != null) {
            welcomeLabel.setText("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        }
    }

    /**
     * Setup table columns
     */
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        patientTable.setItems(patientObservableList);
    }

    /**
     * Load patients into table
     */
    private void loadPatients() {
        List<Patient> patients = patientService.getAllPatients();
        patientObservableList.clear();
        patientObservableList.addAll(patients);
    }

    /**
     * Handle add patient action
     */
    @FXML
    public void handleAddPatient() {
        try {
            Patient patient = createPatientFromForm();
            if (patient != null) {
                patient.setCreatedBy(currentUser != null ? currentUser.getUserId() : null);
                int patientId = patientService.addPatient(patient);
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                         "Patient added successfully with ID: " + patientId);
                clearForm();
                loadPatients();
            }
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    /**
     * Handle update patient action
     */
    @FXML
    public void handleUpdatePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                     "Please select a patient to update.");
            return;
        }

        try {
            Patient patient = createPatientFromForm();
            if (patient != null) {
                patient.setPatientId(selectedPatient.getPatientId());
                patient.setRegistrationDate(selectedPatient.getRegistrationDate());
                patient.setCreatedBy(selectedPatient.getCreatedBy());
                
                boolean success = patientService.updatePatient(patient);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                             "Patient updated successfully.");
                    clearForm();
                    loadPatients();
                }
            }
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    /**
     * Handle delete patient action
     */
    @FXML
    public void handleDeletePatient() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                     "Please select a patient to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Patient");
        confirmAlert.setContentText("Are you sure you want to delete patient: " + 
                                   selectedPatient.getFullName() + "?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean success = patientService.deletePatient(selectedPatient.getPatientId());
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", 
                                 "Patient deleted successfully.");
                        clearForm();
                        loadPatients();
                    }
                } catch (DatabaseException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
                }
            }
        });
    }

    /**
     * Handle refresh action
     */
    @FXML
    public void handleRefresh() {
        patientService.refreshPatients();
        loadPatients();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed", 
                 "Patient list refreshed from database.");
    }

    /**
     * Handle search action
     * Demonstrates Stream usage in service layer
     */
    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadPatients();
            return;
        }

        List<Patient> results = patientService.searchPatientsByName(searchTerm);
        patientObservableList.clear();
        patientObservableList.addAll(results);
    }

    /**
     * Handle age filter
     * Demonstrates Stream filtering by age
     */
    private void handleAgeFilter() {
        String selectedFilter = filterAgeComboBox.getValue();
        List<Patient> filtered;

        switch (selectedFilter) {
            case "0-17 (Child)":
                filtered = patientService.filterPatientsByAge(0, 17);
                break;
            case "18-64 (Adult)":
                filtered = patientService.filterPatientsByAge(18, 64);
                break;
            case "65+ (Senior)":
                filtered = patientService.filterPatientsByAge(65, 150);
                break;
            default:
                filtered = patientService.getAllPatients();
        }

        patientObservableList.clear();
        patientObservableList.addAll(filtered);
    }

    /**
     * Create Patient object from form data
     */
    private Patient createPatientFromForm() throws ValidationException {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        LocalDate dateOfBirth = dateOfBirthPicker.getValue();
        String genderStr = genderComboBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new ValidationException("First name and last name are required.");
        }
        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth is required.");
        }
        if (genderStr == null) {
            throw new ValidationException("Gender is required.");
        }

        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(Patient.Gender.valueOf(genderStr));
        patient.setPhone(phoneField.getText().trim());
        patient.setEmail(emailField.getText().trim());
        patient.setAddress(addressArea.getText().trim());
        patient.setEmergencyContact(emergencyContactField.getText().trim());
        patient.setEmergencyPhone(emergencyPhoneField.getText().trim());
        patient.setBloodType(bloodTypeField.getText().trim().toUpperCase());

        return patient;
    }

    /**
     * Load patient data into form
     */
    private void loadPatientToForm(Patient patient) {
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        dateOfBirthPicker.setValue(patient.getDateOfBirth());
        genderComboBox.setValue(patient.getGender().name());
        phoneField.setText(patient.getPhone());
        emailField.setText(patient.getEmail());
        addressArea.setText(patient.getAddress());
        emergencyContactField.setText(patient.getEmergencyContact());
        emergencyPhoneField.setText(patient.getEmergencyPhone());
        bloodTypeField.setText(patient.getBloodType());
    }

    /**
     * Clear form fields
     */
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        dateOfBirthPicker.setValue(null);
        genderComboBox.setValue(null);
        phoneField.clear();
        emailField.clear();
        addressArea.clear();
        emergencyContactField.clear();
        emergencyPhoneField.clear();
        bloodTypeField.clear();
        patientTable.getSelectionModel().clearSelection();
    }

    /**
     * Show alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
    

