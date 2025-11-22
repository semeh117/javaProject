package com.hms.presentation;

import com.hms.repository.UserDAO;
import com.hms.exception.DatabaseException;
import com.hms.domain.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Login Controller
 * Demonstrates Lambda expressions for event handling
 */
public class LoginController {
	
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button cancelButton;
    
    private static User currentUser;
    private UserDAO userDAO;

    /**
     * Initialize controller
     * Demonstrates Lambda expressions for button event handling
     */
    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        // Lambda expression for login button click
        loginButton.setOnAction(event -> {
            try {
                handleLogin();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Login failed: " + e.getMessage());
            }
        });

        // Lambda expression for cancel button click
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        // Lambda expression for Enter key on password field
        passwordField.setOnAction(event -> {
            try {
                handleLogin();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Login failed: " + e.getMessage());
            }
        });
    }

    /**
     * Handle login action
     * Demonstrates exception handling with SQLException
     */
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please enter both username and password.");
            return;
        }

        try {
            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                currentUser = user;
                openDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                         "Invalid username or password.");
            }
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                     "Error connecting to database: " + e.getMessage());
        }
    }


    /**
     * Open dashboard window
     */
    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/view/Dashboard.fxml"));
            Parent root = loader.load();
            
            DashboardController dashboardController = loader.getController();
            dashboardController.setCurrentUser(currentUser);
            
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Hospital Management System - Dashboard");
            dashboardStage.setScene(new Scene(root, 1200, 800));
            dashboardStage.setMaximized(true);
            dashboardStage.show();
            
            // Close login window
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                     "Failed to open dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show alert dialog
     * @param alertType Type of alert
     * @param title Alert title
     * @param message Alert message
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Get current logged-in user
     * @return Current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}

