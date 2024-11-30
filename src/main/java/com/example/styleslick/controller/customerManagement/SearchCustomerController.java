package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.*;
import com.example.styleslick.service.CustomerService;
import com.example.styleslick.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCustomerController {

    UserSession userSession;
    CustomerService customerService;

    @FXML
    private AnchorPane anchorPane_searchForCustomer;
    @FXML
    private TextField field_username;
    @FXML
    private TextField field_name;
    @FXML
    private TextField field_lastName;
    @FXML
    private TextField field_street;
    @FXML
    private TextField field_ort;
    @FXML
    private TextField field_plz;
    @FXML
    private TextField field_platform;
    @FXML
    private AnchorPane anchorPane_foundedCustomers;
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
         userSession = UserSession.getInstance();
         customerService = CustomerService.getInstance();
    }


    // TODO:: Diese Methode ausprobieren statt executeSearchCustomer()
    @FXML
    private void executeSearchCustomer() {

        //TODO:: Kommentieren!
        column_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        column_ort.setCellValueFactory(new PropertyValueFactory<>("ort"));
        column_plz.setCellValueFactory(new PropertyValueFactory<>("plz"));
        column_platform.setCellValueFactory(new PropertyValueFactory<>("platform"));

        Map<String, String> fields = new HashMap<>();

        fields.put("benutzername", field_username.getText());
        fields.put("name", field_name.getText());
        fields.put("nachname", field_lastName.getText());
        fields.put("strasse", field_street.getText());
        fields.put("plz", field_plz.getText());
        fields.put("ort", field_ort.getText());
        fields.put("gekauft_ueber", field_platform.getText());

        // Bekommt eine Liste aller Customer
        List<Customer> listOfCustomers = customerService.searchCustomer(fields);
        if (listOfCustomers == null || listOfCustomers.isEmpty()) {
            Rules.showErrorAlert("Keinen Kunden gefunden.");
            return;
        }

        // Packt die Liste von Customers in die Tabllenansicht
        ObservableList<Customer> observableList = FXCollections.observableArrayList(listOfCustomers);
        tableView_customer.setItems(observableList);
        anchorPane_searchForCustomer.setVisible(false);
        anchorPane_foundedCustomers.setVisible(true);
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
    private void executeExitSearchCustomer() {
        SceneManager.switchScene("/com/example/styleslick/customerManagement-view.fxml", "Kundenverwaltung");
    }

    @FXML
    private void onKeyPressedEnterExitSearchCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitSearchCustomer();
        }
    }

    @FXML
    private void onMouseClickedExitSearchCustomer(MouseEvent event) {
        executeExitSearchCustomer();
    }



    @FXML
    private void executeExitFoundedCustomer() {
        anchorPane_foundedCustomers.setVisible(false);
        anchorPane_searchForCustomer.setVisible(true);
    }

    @FXML
    private void onKeyPressedEnterExitFoundedCustomer(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            executeExitFoundedCustomer();
        }
    }

    @FXML
    private void onMouseClickedExitFoundedCustomer(MouseEvent event) {
        executeExitFoundedCustomer();
    }

}
