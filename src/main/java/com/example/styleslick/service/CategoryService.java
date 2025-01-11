package com.example.styleslick.service;

import com.example.styleslick.model.Category;
import com.example.styleslick.model.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    TODO:: beim löschen einer Kategorie, muss noch geprüft werden ob die Kategorie in abhängigkeiten zu einem Artikel steht
     wenn JA nachfragen ob alle verbundenen Artikel mit gelöscht werden sollen oder abbrechen.
     vllt auch eine Meldung einfach ausgeben das es noch Artikel gibt mit einer abhängigkeit zu der Kategorie
     und erst die Artikel manuel gelöscht werden müssen.
 */

public class CategoryService {

    private static CategoryService categoryService;
    private Database database;
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);


    private CategoryService() {
    }

    public static synchronized CategoryService getInstance() {
        if (categoryService == null) {
            categoryService = new CategoryService();
        }
        return categoryService;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }


    public boolean addCategory(Map<String, String> fields) {
        logger.info("Methode addCategory() START.");
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte tragen Sie was ein.");
            logger.warn("Liste mit ausgefüllten Feldern ist leer ENDE.\n");
            return false;
        }

        if (!database.addCategory(filledFields)) {
            AlertService.showErrorAlert("Fehler beim erstellen.");
            logger.warn("Fehler, Kategorie wurde nicht in die Datenbank geschieben ENDE.\n");
            return false;
        }

        AlertService.showConfirmAlert("Die Kategorie wurde erfolgreich erstellt.");
        logger.info("Methode addCategory() erfolgreich ENDE.\n");
        return true;
    }


    public boolean updateCategory(Map<String, String> fields, int categoryID) {
        logger.info("Methode updateCategory() START.");
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie was ein um die Kategorie zu bearbeiten.");
            logger.warn("Liste mit ausgefüllten Feldern ist leer ENDE\n");
            return false;
        }

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich die Kategorie mit der Kategorie-Nr " + categoryID + " bearbeiten?")) {
            AlertService.showErrorAlert("Artikel wird nicht bearbeitet.");
            logger.debug("Kategorie bearbeitung wurde durch den Benutzer beendet ENDE.\n");
            return false;
        }

        if (!database.updateCategory(filledFields, categoryID)) {
            AlertService.showErrorAlert("Fehler beim bearbeiten der Kategorie.");
            logger.warn("Kategorie wurde nicht in die Datenbank geschrieben ENDE.\n");
            return false;
        }

        logger.info("Methode updateCategory() erfolgreich ENDE.\n");
        return true;
    }


    public boolean deleteCategory(int categoryID) {
        logger.info("Methode deleteCategory() START.");

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich die Kategorie mit der Kategorie-Nr '" + categoryID + "' löschen?")) {
            AlertService.showErrorAlert("Kategorie wird nicht gelöscht.");
            logger.debug("Kategorie löschen wurde durch Benutzer beendet ENDE.\n");
            return false;
        }

        if (!database.deleteCategory(categoryID)) {
            AlertService.showErrorAlert("Fehler beim löschen der Kategorie.");
            logger.warn("Kategorie wurde nicht in der Datenbank gelöscht ENDE.\n");
            return false;
        }

        AlertService.showConfirmAlert("Kategorie wurde erfolgreich gelöscht.");
        logger.info("Methode deleteCategory() erfolgreich ENDE.\n");
        return true;
    }


    public List<Category> getAllCategories() {
        logger.info("Methode getAllCategories() START.");
        List<Category> listOfCategories = database.getAllCategories();
        if (listOfCategories == null || listOfCategories.isEmpty()) {
            AlertService.showErrorAlert("Fehler beim Laden der Kategorien.");
            logger.warn("Fehler beim Laden der Kategorien Liste ist leer ENDE.\n");
            return listOfCategories;
        }

        logger.info("Methode getAllCategories() erfolgreich ENDE.\n");
        return listOfCategories;
    }


    public void clearSession() {
        categoryService = null;
        database = null;
    }
}
