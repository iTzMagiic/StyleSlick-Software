package com.example.styleslick.service;

import com.example.styleslick.model.Category;
import com.example.styleslick.model.Database;
import javafx.css.Rule;
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
            RulesService.showErrorAlert("Bitte tragen Sie was ein.");
            logger.warn("Liste mit ausgefüllten Feldern ist leer.");
            return false;
        }

        if (!database.addCategory(filledFields)) {
            RulesService.showErrorAlert("Fehler beim erstellen.");
            logger.warn("Kategorie wurde nicht erfolgreich in die Datenbank geschieben. ENDE.");
            return false;
        }

        RulesService.showConfirmAlert("Die Kategorie wurde erfolgreich erstellt.");
        logger.info("Methode addCategory() erfolgreich ENDE.");
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
            RulesService.showErrorAlert("Bitte geben Sie was ein um die Kategorie zu bearbeiten.");
            logger.warn("Liste mit ausgefüllten Feldern ist leer.");
            return false;
        }

        if (!RulesService.showConfirmAlertResult("Möchten Sie wirklich die Kategorie mit der Kategorie-Nr " + categoryID + " bearbeiten?")) {
            RulesService.showErrorAlert("Artikel wird nicht bearbeitet.");
            logger.debug("Kategorie bearbeitung wurde durch Benutzer beendet ENDE.");
            return false;
        }

        if (!database.updateCategory(filledFields, categoryID)) {
            RulesService.showErrorAlert("Fehler beim bearbeiten der Kategorie.");
            logger.warn("Kategorie wurde nicht in die Datenbank geschrieben. ENDE.");
            return false;
        }

        logger.info("Methode updateCategory() erfolgreich ENDE.");
        return true;
    }


    public boolean deleteCategory(int categoryID) {
        logger.info("Methode deleteCategory() START.");

        if (!RulesService.showConfirmAlertResult("Möchten Sie wirklich die Kategorie mit der Kategorie-Nr '" + categoryID + "' löschen?")) {
            RulesService.showErrorAlert("Kategorie wird nicht gelöscht.");
            logger.debug("Kategorie löschen wurde durch Benutzer beendet ENDE.");
            return false;
        }

        if (!database.deleteCategory(categoryID)) {
            RulesService.showErrorAlert("Fehler beim löschen der Kategorie.");
            logger.warn("Kategorie wurde nicht in der Datenbank gelöscht ENDE.");
            return false;
        }

        RulesService.showConfirmAlert("Kategorie wurde erfolgreich gelöscht.");
        logger.info("Methode deleteCategory() erfolgreich ENDE.");
        return true;
    }


    public List<Category> getAllCategories() {
        logger.info("Methode getAllCategories() START.");
        List<Category> listOfCategories = database.getAllCategories();
        if (listOfCategories == null || listOfCategories.isEmpty()) {
            RulesService.showErrorAlert("Fehler beim Laden der Kategorien.");
            logger.warn("Fehler beim Laden der Kategorien Liste ist leer ENDE.");
            return listOfCategories;
        }

        logger.info("Methode getAllCategories() erfolgreich ENDE.");
        return listOfCategories;
    }


    public void clearSession() {
        categoryService = null;
        database = null;
    }
}
