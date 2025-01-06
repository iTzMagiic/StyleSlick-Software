package com.example.styleslick.controller;

import com.example.styleslick.model.Category;
import com.example.styleslick.service.CategoryService;
import com.example.styleslick.service.RulesService;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CategoryManagementController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(CategoryManagementController.class);
    CategoryService categoryService;

    @FXML
    private TableColumn<Category, Integer> column_categoryID;

    @FXML
    private TableColumn<Category, String> column_categoryName;

    @FXML
    private TextField field_categoryName;

    @FXML
    private TableView<Category> tableView_categories;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Methode initialize START.");
        categoryService = CategoryService.getInstance();

        tableView_categories.getSelectionModel().selectedItemProperty();
        executeShowAllCategories();
        logger.info("Methode initialize erfolgreich END.");
    }



    private void executeShowAllCategories() {
        logger.info("Methode executeShowAllCategories() START.");
        column_categoryID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        column_categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));

        ObservableList<Category> observableList = FXCollections.observableArrayList(categoryService.getAllCategories());
        tableView_categories.setItems(observableList);
        logger.info("Methode executeShowAllCategories() erfolgreich ENDE.");
    }


    private void executeAddCategory() {
        logger.info("Methode executeAddCategory() START.");
        Map<String, String> fields = new HashMap<>();

        fields.put("name", field_categoryName.getText());

        if (!categoryService.addCategory(fields)) {
            return;
        }

        field_categoryName.clear();
        executeShowAllCategories();
        logger.info("Methode executeAddCategory() erfolgreich ENDE.");
    }


    private void executeUpdateCategory() {
        logger.info("Methode executeUpdateCategory() START.");
        Map<String, String> fields = new HashMap<>();

        Category selectedCategory = tableView_categories.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            RulesService.showErrorAlert("Bitte wählen Sie eine Kategorie aus der Tabelle aus, um ihn zu bearbeiten.");
            logger.debug("Keine Kategorie aus der Tabelle ausgewählt. selectedCategory = {} ENDE", selectedCategory);
            return;
        }

        fields.put("name", field_categoryName.getText());

        if (!categoryService.updateCategory(fields, selectedCategory.getID())) {
            return;
        }

        field_categoryName.clear();
        executeShowAllCategories();
        logger.info("Methode executeUpdateCategory() erfolgreich ENDE.");
    }


    private void executeDeleteCategory() {
        logger.info("Methode executeDeleteCategory() START.");
        Category selectedCategory = tableView_categories.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            RulesService.showErrorAlert("Bitte wählen Sie eine Kategorie aus der Tabelle aus, um ihn zu löschen.");
            logger.debug("Keine Kategorie aus der Tabelle ausgewählt. selectedCategory = {} ENDE", selectedCategory);
            return;
        }

        if (categoryService.deleteCategory(selectedCategory.getID())) {
            executeShowAllCategories();
        }
    }


    private void executeExitCategoryManagement() {
        categoryService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/loggedIn-view.fxml", "Willkommen");
    }



    @FXML
    void onKeyPressedEnterAddCategory(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddCategory();
        }
    }

    @FXML
    void onKeyPressedEnterDeleteCategory(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteCategory();
        }
    }

    @FXML
    void onKeyPressedEnterExitCategoryManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitCategoryManagement();
        }
    }

    @FXML
    void onKeyPressedEnterShowAllCategories(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllCategories();
        }
    }

    @FXML
    void onKeyPressedEnterUpdateCategory(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeUpdateCategory();
        }
    }

    @FXML
    void onMouseClickedAddCategory(MouseEvent event) {
        executeAddCategory();
    }

    @FXML
    void onMouseClickedDeleteCategory(MouseEvent event) {
        executeDeleteCategory();
    }

    @FXML
    void onMouseClickedExitCategoryManagement(MouseEvent event) {
        executeExitCategoryManagement();
    }

    @FXML
    void onMouseClickedShowAllCategories(MouseEvent event) {
        executeShowAllCategories();
    }

    @FXML
    void onMouseClickedUpdateCategory(MouseEvent event) {
        executeUpdateCategory();
    }
}
