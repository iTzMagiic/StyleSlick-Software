package com.example.styleslick.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertService {

    public static void showErrorAlert(String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Standardmäßig ein Info-Dialog
        alert.setTitle("Fehler!");
        alert.setHeaderText(header);
        alert.showAndWait(); // Zeigt den Dialog an und wartet, bis der Benutzer ihn schließt
    }

    public static void showConfirmAlert(String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Erfolgreich!");
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public static boolean showConfirmAlertResult(String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sicher?");
        alert.setHeaderText(header);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty()) {
            return false;
        } else if (result.get() == ButtonType.OK) {
            return true;
        } else if (result.get() == ButtonType.CANCEL) {
            return false;
        }

        return false;
    }
}
