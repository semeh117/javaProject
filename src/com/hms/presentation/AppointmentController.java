package com.hms.presentation;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.hms.domain.Appointment;
import com.hms.service.DataService;


import javafx.collections.FXCollections;




public class AppointmentController {
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> patientColumn;
    @FXML private TableColumn<Appointment, String> doctorColumn;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    @FXML
    public void initialize() {
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointmentsTable.setItems(
                FXCollections.observableArrayList(
                    DataService.getInstance().getAppointments()));
          
    }


}
