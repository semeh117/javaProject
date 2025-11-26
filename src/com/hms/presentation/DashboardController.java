package com.hms.presentation;

import com.hms.service.DataService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label totalPatientsLabel;
    @FXML private Label totalDoctorsLabel;
    @FXML private Label activeAppointmentsLabel;
    @FXML private Label revenueLabel;

    @FXML
    public void initialize() {
        var stats = DataService.getInstance().getStats();
        totalPatientsLabel.setText(String.valueOf(stats.totalPatients));
        totalDoctorsLabel.setText(String.valueOf(stats.totalDoctors));
        activeAppointmentsLabel.setText(String.valueOf(stats.activeAppointments));
        revenueLabel.setText("$" + stats.revenue);
    }
}
