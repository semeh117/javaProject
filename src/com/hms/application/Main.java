package com.hms.application;

import com.hms.repository.DBHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database connection
        DBHandler.getInstance();
        
        // Load login FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/view/Login.fxml"));
        Parent root = loader.load();
        
        // Setup primary stage
        primaryStage.setTitle("Hospital Management System - Login");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Close database connection when application exits
        DBHandler.getInstance().closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

