package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.Customer;
import com.example.styleslick.service.CustomerService;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ShowCustomersController {

    @FXML
    private TableView<Customer> tableView_customer;
    @FXML
    private TableColumn<Customer, String> column_username;
    @FXML
    private TableColumn<Customer, String> column_name;
    @FXML
    private TableColumn<Customer, String> column_lastName;
    @FXML
    private TableColumn<Customer, String> column_street;
    @FXML
    private TableColumn<Customer, Integer> column_plz;
    @FXML
    private TableColumn<Customer, String> column_ort;
    @FXML
    private TableColumn<Customer, String> column_platform;


    public void initialize() {
        column_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        column_ort.setCellValueFactory(new PropertyValueFactory<>("ort"));
        column_plz.setCellValueFactory(new PropertyValueFactory<>("plz"));
        column_platform.setCellValueFactory(new PropertyValueFactory<>("platform"));


        CustomerService customerService = CustomerService.getInstance();

        ObservableList<Customer> observableList = FXCollections.observableArrayList(customerService.getCustomers());
        tableView_customer.setItems(observableList);
    }



    @FXML
    private void executeExitShowCustomer() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung");
    }

    @FXML
    private void onKeyPressedEnterExitShowCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitShowCustomer();
        }
    }

    @FXML
    private void onMouseClickedExitShowCustomer(MouseEvent event) {
        executeExitShowCustomer();
    }
}
