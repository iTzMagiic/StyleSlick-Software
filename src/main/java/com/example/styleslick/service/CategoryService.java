package com.example.styleslick.service;

import com.example.styleslick.model.Category;
import com.example.styleslick.model.Database;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryService {

    private static CategoryService categoryService;
    private Database database;


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
        Map<String, String> filledFields = new HashMap<>();


        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }


        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte tragen Sie was ein.");
            return false;
        }

        if (database.isCategoryNameExist(filledFields.get("name"))) {
            AlertService.showErrorAlert("Die Kategorie existiert bereits.");
            return false;
        }

        if (!database.addCategory(filledFields)) {
            AlertService.showErrorAlert("Ein Fehler ist aufgetreten.");
            return false;
        }

        AlertService.showConfirmAlert("Die Kategorie wurde erfolgreich erstellt.");
        return true;
    }


    public boolean updateCategory(Map<String, String> fields, Category category) {
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }


        if (filledFields.isEmpty()) {
            AlertService.showErrorAlert("Bitte geben Sie was ein um die Kategorie zu bearbeiten.");
            return false;
        }

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich die Kategorie bearbeiten?")) {
            AlertService.showErrorAlert("Kategorie wird nicht bearbeitet.");
            return false;
        }

        if (!database.updateCategory(filledFields, category.getID())) {
            AlertService.showErrorAlert("Fehler beim bearbeiten der Kategorie.");
            return false;
        }

        AlertService.showConfirmAlert("Die Kategorie wurde erfolgreich bearbeitet.");
        return true;
    }


    public List<Category> searchCategory(String name) {
        List<Category> listOfCategories;

        listOfCategories = database.searchCategory(name);

        if (listOfCategories.isEmpty()) {
            AlertService.showErrorAlert("Keine passende Kategorie gefunden.");
        }

        return listOfCategories;
    }


    public boolean deleteCategory(Category category) {

        if (!AlertService.showConfirmAlertResult("Möchten Sie wirklich die Kategorie löschen?")) {
            AlertService.showErrorAlert("Kategorie wird nicht gelöscht.");
            return false;
        }

        if (database.hasCategoryDependencies(category.getID())) {
            AlertService.showErrorAlert("Bitte Löschen Sie zu erst alle Artikel die eine Abhängigkeit zu der Kategorie haben.");
            return false;
        }

        if (!database.deleteCategory(category.getID())) {
            AlertService.showErrorAlert("Fehler beim löschen der Kategorie.");
            return false;
        }

        AlertService.showConfirmAlert("Die Kategorie wurde erfolgreich gelöscht.");
        return true;
    }


    public List<Category> getAllCategories() {
        return database.getAllCategories();
    }


    public void clearSession() {
        categoryService = null;
        database = null;
    }
}
