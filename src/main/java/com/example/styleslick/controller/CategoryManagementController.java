package com.example.styleslick.controller;

import com.example.styleslick.model.Category;
import com.example.styleslick.service.CategoryService;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.utils.SceneManager;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CategoryManagementController implements Initializable {

    CategoryService categoryService;


    @FXML
    private JFXButton button_showAll;
    @FXML
    private JFXButton button_search;
    @FXML
    private TableColumn<Category, String> column_categoryName;
    @FXML
    private TextField field_categoryName;
    @FXML
    private TableView<Category> tableView_categories;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryService = CategoryService.getInstance();

        tableView_categories.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                field_categoryName.setText(tableView_categories.getSelectionModel().getSelectedItem().getName());
            }
        });

        executeShowAllCategories();
    }


    private void executeShowAllCategories() {
        field_categoryName.clear();
        button_showAll.setMouseTransparent(true);

        ObservableList<Category> observableList = FXCollections.observableArrayList(categoryService.getAllCategories());

        Task<Void> showAllTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    column_categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));

                    tableView_categories.setItems(observableList);
                    button_showAll.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(showAllTask).start();
    }


    private void executeAddCategory() {
        Map<String, String> fields = new HashMap<>();

        fields.put("name", field_categoryName.getText());

        if (!categoryService.addCategory(fields)) {
            return;
        }

        executeShowAllCategories();
    }


    private void executeUpdateCategory() {
        Map<String, String> fields = new HashMap<>();

        Category selectedCategory = tableView_categories.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            AlertService.showErrorAlert("Bitte wählen Sie eine Kategorie aus der Tabelle aus, um ihn zu bearbeiten.");
            return;
        }

        fields.put("name", field_categoryName.getText());

        if (!categoryService.updateCategory(fields, selectedCategory)) {
            return;
        }

        executeShowAllCategories();
    }


    private void executeSearchCategory() {
        String name = field_categoryName.getText();

        if (name == null || name.isEmpty()) {
            AlertService.showErrorAlert("Bitte tragen Sie was ein um nach einer Kategorie zu suchen.");
            return;
        }

        List<Category> listOfCategories = categoryService.searchCategory(name);

        if (listOfCategories.isEmpty()) {
            return;
        }

        button_search.setMouseTransparent(true);

        Task<Void> searchTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    ObservableList<Category> observableList = FXCollections.observableArrayList(listOfCategories);
                    tableView_categories.setItems(observableList);
                    button_search.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(searchTask).start();
    }


    private void executeDeleteCategory() {
        Category selectedCategory = tableView_categories.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            AlertService.showErrorAlert("Bitte wählen Sie eine Kategorie aus der Tabelle aus, um ihn zu löschen.");
            return;
        }

        if (categoryService.deleteCategory(selectedCategory)) {
            executeShowAllCategories();
        }
    }


    private void executeExitCategoryManagement() {
        categoryService.clearSession();
        SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen", false);
    }


    @FXML
    private void onKeyPressedEnterAddCategory(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddCategory();
        }
    }

    @FXML
    private void onKeyPressedEnterDeleteCategory(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteCategory();
        }
    }

    @FXML
    private void onKeyPressedEnterExitCategoryManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitCategoryManagement();
        }
    }

    @FXML
    private void onKeyPressedEnterShowAllCategories(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllCategories();
        }
    }

    @FXML
    private void onKeyPressedEnterUpdateCategory(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeUpdateCategory();
        }
    }

    @FXML
    private void onKeyPressedEnterSearchCategory(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeSearchCategory();
        }
    }

    @FXML
    private void onMouseClickedAddCategory(MouseEvent event) {
        executeAddCategory();
    }

    @FXML
    private void onMouseClickedDeleteCategory(MouseEvent event) {
        executeDeleteCategory();
    }

    @FXML
    private void onMouseClickedExitCategoryManagement(MouseEvent event) {
        executeExitCategoryManagement();
    }

    @FXML
    private void onMouseClickedShowAllCategories(MouseEvent event) {
        executeShowAllCategories();
    }

    @FXML
    private void onMouseClickedUpdateCategory(MouseEvent event) {
        executeUpdateCategory();
    }

    @FXML
    private void onMouseClickedSearchCategory(MouseEvent event) {
        executeSearchCategory();
    }
}
