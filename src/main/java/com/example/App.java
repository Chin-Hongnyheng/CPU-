package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    // Stage -> Scene -> Root
    // All Element -> Root
    // Root -> Scene
    // Scene -> Stage
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CPU.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("CPU Algorithms");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
