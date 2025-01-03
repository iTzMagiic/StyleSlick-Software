package com.example.styleslick;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.styleslick.utils.SceneManager;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager.setStage(primaryStage);
        SceneManager.switchScene("/com/example/styleslick/login-view.fxml", "Einloggen");
    }

    public static void main(String[] args) {
        launch();
    }
}