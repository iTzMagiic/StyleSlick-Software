package com.example.styleslick.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    private static Stage stage; // Speichert die Hauptbühne (Stage)

    /**
     * Setzt die Hauptbühne (Stage) der Anwendung.
     * Diese Methode muss von der Main-Klasse aufgerufen werden, um den SceneManager mit der Haupt-Stage zu verbinden.
     *
     * @param primaryStage Die Hauptbühne der Anwendung.
     */
    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void setWindowIcon(String iconPath) {
        stage.getIcons().add(new Image(Objects.requireNonNull(SceneManager.class.getResourceAsStream(iconPath))));
    }


    /**
     * Wechselt die Szene, indem die angegebene FXML-Datei geladen wird.
     *
     * @param fxmlPath Der relative Pfad zur FXML-Datei (z. B. "/com/example/view/login-view.fxml").
     */
    public static void switchScene(String fxmlPath, String title, boolean resizable) {
        try {
            // Lade die FXML-Datei
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load(); // Parent root ist das Wurzelelement z.B. AnchorPane oder VBox
            // Erstelle eine neue Szene und setze sie in die Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);


            stage.centerOnScreen();
            stage.setTitle(title);
            stage.setResizable(resizable);


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
