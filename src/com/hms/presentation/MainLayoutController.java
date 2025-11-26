package com.hms.presentation;

import java.io.IOException;

import com.hms.application.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class MainLayoutController {

    @FXML
    private BorderPane mainLayout;

    @FXML
    public void initialize() {
        // Load Dashboard by default
        loadView("Dashboard");
    }

    @FXML
    private void showDashboard() { loadView("Dashboard"); }

    @FXML
    private void showPatients() { loadView("Patient"); }

    @FXML
    private void showAppointments() { loadView("Appointment"); }

    @FXML
    private void showStaff() { loadView("Staff"); }

    @FXML
    private void handleLogout() {
        try {
            App.setRoot("/com/hms/view/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/com/hms/view/" + viewName + ".fxml")
            );

            Parent view = loader.load();
            mainLayout.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
