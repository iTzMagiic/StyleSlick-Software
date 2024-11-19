package com.example.styleslick;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.styleslick.utils.SceneManager;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager.setStage(primaryStage);
        SceneManager.switchScene("/com/example/styleslick/login-view.fxml", "Erste Seite");
    }

    public static void main(String[] args) {
        launch();
    }
}