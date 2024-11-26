package com.example.styleslick.controller.customerManagement;

import com.example.styleslick.model.Customer;
import com.example.styleslick.model.CustomerService;
import com.example.styleslick.model.UserSession;
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

public class SearchCustomerController {

    UserSession userSession;

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
    }



    @FXML
    private void executeSearchCustomer() {

        String username = field_username.getText();
        String name = field_name.getText();
        String lastName = field_lastName.getText();
        String street = field_street.getText();
        String plz = field_plz.getText();
        String ort = field_ort.getText();
        String platform = field_platform.getText();


        column_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        column_ort.setCellValueFactory(new PropertyValueFactory<>("ort"));
        column_plz.setCellValueFactory(new PropertyValueFactory<>("plz"));
        column_platform.setCellValueFactory(new PropertyValueFactory<>("platform"));

        CustomerService customerService = CustomerService.getInstance();


        ObservableList<Customer> observableList = FXCollections.observableArrayList(customerService.searchCustomer(
                "benutzername", username,
                "name", name,
                "nachname", lastName,
                "strasse", street,
                "plz", plz,
                "ort", ort,
                "gekauft_ueber", platform));

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
