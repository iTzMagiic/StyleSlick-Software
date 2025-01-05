package com.example.styleslick.service;

import com.example.styleslick.model.Category;
import com.example.styleslick.model.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {
    private static CategoryService categoryService;
    private Database database;
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);


    private CategoryService() {}

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
        logger.info("Methode addCategory() START. Parameter: fields = {}", fields);
        Map<String, String> filledFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                filledFields.put(entry.getKey(), entry.getValue());
            }
        }

        if (filledFields.isEmpty()) {
            RulesService.showErrorAlert("Bitte tragen Sie was ein.");
            logger.warn("Liste ist leer.");
            return false;
        }

        if (!database.addCategory(filledFields)) {
            RulesService.showErrorAlert("Fehler beim erstellen.");
            logger.warn("Methode addCategory() ENDE.");
            return false;
        }

        RulesService.showConfirmAlert("Die Kategorie wurde erfolgreich erstellt.");
        logger.info("Methode addCategory() erfolgreich ENDE.");
        return true;
    }


    public List<Category> getAllCategories() {
        logger.info("Methode getAllCategories() START.");
        List<Category> listOfCategories = database.getAllCategories();
        if (listOfCategories == null || listOfCategories.isEmpty()) {
            RulesService.showErrorAlert("Fehler beim Laden der Kategorien.");
            logger.warn("Fehler beim Laden der Kategorien. listOfCategories = {}", listOfCategories);
        }
        logger.info("Methode getAllCategories() ENDE.");
        return database.getAllCategories();
    }


    public void clearSession() {
        logger.info("Methode clearSession() START.");
        categoryService = null;
        database = null;
        logger.info("Methode ClearSession() erfolgreich ENDE.");
    }
}
