package com.hms.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Make sure Login.fxml exists at /resources/com/hms/views/Login.fxml
        Parent root = FXMLLoader.load(App.class.getResource("/com/hms/view/Login.fxml"));
        scene = new Scene(root);

        // Optional stylesheet
        scene.getStylesheets().add(App.class.getResource("/com/hms/view/styles.css").toExternalForm());

        stage.setTitle("HMS");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(App.class.getResource(fxmlPath));
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}
