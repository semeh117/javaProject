package com.hms.presentation;

import com.hms.service.DataService;
import com.hms.domain.Patient;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientController {

    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, String> nameColumn;
    @FXML private TableColumn<Patient, Integer> ageColumn;
    @FXML private TableColumn<Patient, String> genderColumn;
    @FXML private TableColumn<Patient, String> contactColumn;
    @FXML private TableColumn<Patient, String> registrationColumn;

    @FXML
    public void initialize() {
        // Patient.java has getFullName(), getAge(), getGender(), getPhone(), getRegistrationDate()
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        registrationColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        patientsTable.setItems(FXCollections.observableArrayList(DataService.getInstance().getPatients()));
    }
}
