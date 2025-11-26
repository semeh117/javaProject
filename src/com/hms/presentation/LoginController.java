package com.hms.presentation;

import com.hms.domain.User;
import com.hms.service.DataService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String identifier = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (identifier.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username/email and password");
            errorLabel.setVisible(true);
            return;
        }

        try {
            User user = DataService.getInstance().authenticate(identifier, password);

            if (user != null) {
                // Navigate to main layout by changing the current scene root
                Parent root = FXMLLoader.load(getClass().getResource("/com/hms/view/main_layout.fxml"));
                emailField.getScene().setRoot(root);
            } else {
                errorLabel.setText("Invalid credentials");
                errorLabel.setVisible(true);
            }
        } catch (Exception e) {
            errorLabel.setText("An unexpected error occurred.");
            errorLabel.setVisible(true);
            e.printStackTrace();
        }
    }

}

