package com.example.styleslick.controller;

import com.example.styleslick.model.Customer;
import com.example.styleslick.service.CustomerService;
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

public class CustomerManagementController implements Initializable {

    private CustomerService customerService;

    @FXML
    private TableView<Customer> tableView_customer;
    @FXML
    private TableColumn<Customer, String> column_username;
    @FXML
    private TableColumn<Customer, String> column_first_name;
    @FXML
    private TableColumn<Customer, String> column_last_name;
    @FXML
    private TableColumn<Customer, String> column_street;
    @FXML
    private TableColumn<Customer, String> column_postal_code;
    @FXML
    private TableColumn<Customer, String> column_city;
    @FXML
    private TableColumn<Customer, String> column_purchased_from;
    @FXML
    private TableColumn<Customer, Integer> column_customer_number;
    @FXML
    private TableColumn<Customer, String> column_country;
    @FXML
    private TextField field_username;
    @FXML
    private TextField field_first_name;
    @FXML
    private TextField field_last_name;
    @FXML
    private TextField field_street;
    @FXML
    private TextField field_postal_code;
    @FXML
    private TextField field_city;
    @FXML
    private TextField field_country;
    @FXML
    private TextField field_purchased_from;
    @FXML
    private JFXButton button_add;
    @FXML
    private JFXButton button_delete;
    @FXML
    private JFXButton button_exit;
    @FXML
    private JFXButton button_search;
    @FXML
    private JFXButton button_showAll;
    @FXML
    private JFXButton button_update;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerService = CustomerService.getInstance();

        tableView_customer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Customer doubleClickedCustomer = tableView_customer.getSelectionModel().getSelectedItem();
                field_username.setText(doubleClickedCustomer.getUsername());
                field_first_name.setText(doubleClickedCustomer.getFirstName());
                field_last_name.setText(doubleClickedCustomer.getLastName());
                field_street.setText(doubleClickedCustomer.getStreet());
                field_postal_code.setText(doubleClickedCustomer.getPostalCode());
                field_city.setText(doubleClickedCustomer.getCity());
                field_country.setText(doubleClickedCustomer.getCountry());
                field_purchased_from.setText(doubleClickedCustomer.getPurchasedFrom());
            }
        });

        executeShowAllCustomers();
    }


    private void executeShowAllCustomers() {
        button_showAll.setMouseTransparent(true);

        ObservableList<Customer> observableList = FXCollections.observableArrayList(customerService.getCustomers());

        Task<Void> showAllTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    clearFields();

                    column_username.setCellValueFactory(new PropertyValueFactory<>("username"));
                    column_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                    column_last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                    column_street.setCellValueFactory(new PropertyValueFactory<>("street"));
                    column_city.setCellValueFactory(new PropertyValueFactory<>("city"));
                    column_postal_code.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
                    column_purchased_from.setCellValueFactory(new PropertyValueFactory<>("purchasedFrom"));
                    column_customer_number.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
                    column_country.setCellValueFactory(new PropertyValueFactory<>("country"));


                    tableView_customer.setItems(observableList);
                    button_showAll.setMouseTransparent(false);
                });

                return null;
            }
        };

        new Thread(showAllTask).start();
    }


    private void executeAddCustomer() {
        Map<String, String> fields = new HashMap<>();

        fields.put("username", field_username.getText());
        fields.put("first_name", field_first_name.getText());
        fields.put("last_name", field_last_name.getText());
        fields.put("street", field_street.getText());
        fields.put("postal_code", field_postal_code.getText());
        fields.put("city", field_city.getText());
        fields.put("country", field_country.getText());
        fields.put("purchased_from", field_purchased_from.getText());

        if (!customerService.addCustomer(fields)) {
            return;
        }

        executeShowAllCustomers();
    }


    private void executeUpdateCustomer() {
        Customer selectedCustomer = tableView_customer.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            AlertService.showErrorAlert("Bitte wählen Sie einen Kunden aus der Tabelle aus, um Ihn zu bearbeiten.");
            return;
        }

        Map<String, String> fields = new HashMap<>();


        fields.put("username", field_username.getText());
        fields.put("first_name", field_first_name.getText());
        fields.put("last_name", field_last_name.getText());
        fields.put("street", field_street.getText());
        fields.put("postal_code", field_postal_code.getText());
        fields.put("city", field_city.getText());
        fields.put("country", field_country.getText());
        fields.put("purchased_from", field_purchased_from.getText());


        if (!customerService.updateCustomer(fields, selectedCustomer.getCustomerID())) {
            return;
        }

        executeShowAllCustomers();
    }


    private void executeSearchCustomer() {
        Map<String, String> fields = new HashMap<>();

        fields.put("username", field_username.getText());
        fields.put("first_name", field_first_name.getText());
        fields.put("last_name", field_last_name.getText());
        fields.put("street", field_street.getText());
        fields.put("postal_code", field_postal_code.getText());
        fields.put("city", field_city.getText());
        fields.put("country", field_country.getText());
        fields.put("purchased_from", field_purchased_from.getText());


        List<Customer> listOfCustomers = customerService.searchCustomer(fields);


        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            return;
        }

        button_search.setMouseTransparent(true);

        Task<Void> searchTask = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    ObservableList<Customer> observableList = FXCollections.observableArrayList(listOfCustomers);
                    tableView_customer.setItems(observableList);
                    button_search.setMouseTransparent(false);
                });

                return null;
            }

            ;
        };

        new Thread(searchTask).start();
    }


    public void executeDeleteCustomer() {
        Customer selectedCustomer = tableView_customer.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            AlertService.showErrorAlert("Bitte wählen Sie einen Kunden aus der Tabelle aus, um Ihn zu löschen.");
            return;
        }

        if (!customerService.deleteCustomer(selectedCustomer.getCustomerID(), selectedCustomer.getCustomerNumber())) {
            return;
        }

        executeShowAllCustomers();
    }


    private void executeExitCustomerManagement() {
        CustomerService.getInstance().clearSession();
        SceneManager.switchScene("/com/example/styleslick/Home-view.fxml", "Willkommen");
    }

    @FXML
    private void onKeyPressedUpdateCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeUpdateCustomer();
        }
    }


    @FXML
    private void onMouseClickedUpdateCustomer(MouseEvent event) {
        executeUpdateCustomer();
    }


    @FXML
    private void onKeyPressedDeleteCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeDeleteCustomer();
        }
    }


    @FXML
    private void onMouseCLickedDeleteCustomer(MouseEvent event) {
        executeDeleteCustomer();
    }


    @FXML
    private void onKeyPressedEnterShowAllCustomers(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeShowAllCustomers();
        }
    }


    @FXML
    private void onMouseClickedShowAllCustomers(MouseEvent event) {
        executeShowAllCustomers();
    }


    @FXML
    private void onKeyPressedEnterSearchCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeSearchCustomer();
        }
    }

    @FXML
    private void onMouseClickedSearchCustomer(MouseEvent event) {
        executeSearchCustomer();
    }


    @FXML
    private void onKeyPressedEnterAddCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeAddCustomer();
        }
    }

    @FXML
    private void onMouseClickedAddCustomer(MouseEvent event) {
        executeAddCustomer();
    }


    @FXML
    private void onKeyPressedEnterExitCustomerManagement(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitCustomerManagement();
        }
    }

    @FXML
    private void onMouseClickedExitCustomerManagement(MouseEvent event) {
        executeExitCustomerManagement();
    }


    private void clearFields() {
        field_city.clear();
        field_country.clear();
        field_first_name.clear();
        field_last_name.clear();
        field_postal_code.clear();
        field_purchased_from.clear();
        field_street.clear();
        field_username.clear();
    }
}
