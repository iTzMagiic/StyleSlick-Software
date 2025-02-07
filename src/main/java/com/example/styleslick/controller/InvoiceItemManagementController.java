package com.example.styleslick.controller;

import com.example.styleslick.model.Invoice;
import com.example.styleslick.model.InvoiceItem;
import com.example.styleslick.service.AlertService;
import com.example.styleslick.service.InvoiceService;
import com.example.styleslick.utils.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class InvoiceItemManagementController implements Initializable {

    private InvoiceService invoiceService;
    private Invoice invoice;

    @FXML
    private TableColumn<InvoiceItem, Integer> column_amount;
    @FXML
    private TableColumn<InvoiceItem, String> column_articleNumber;
    @FXML
    private TableColumn<InvoiceItem, String> column_name;
    @FXML
    private TextField field_amount;
    @FXML
    private TextField field_articleNumber;
    @FXML
    private TableView<InvoiceItem> tableView_invoiceItem;
    @FXML
    private Label label_invoiceNumber;
    @FXML
    private Label label_customerNumber;
    @FXML
    private Label label_date;




    public void initialize(URL location, ResourceBundle resources) {
        invoiceService = InvoiceService.getInstance();
        invoice = invoiceService.getCurrentInvoice();

        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        label_date.setText(todayDate.format(formatter));

        executeShowAllItems();
    }



    private void executeShowAllItems() {
        List<InvoiceItem> listOfInvoiceItems;

        listOfInvoiceItems = invoiceService.getInvoiceItems(invoice.getInvoiceID());

        if (listOfInvoiceItems.isEmpty()) {
            return;
        }


        ObservableList<InvoiceItem> observableList = FXCollections.observableArrayList(listOfInvoiceItems);


        Task<Void> showAllTask = new Task<>() {

            @Override
            protected Void call() {

                Platform.runLater(() -> {
                    label_invoiceNumber.setText(invoice.getInvoiceNumber());
                    label_customerNumber.setText(invoice.getCustomerNumber());

                    column_articleNumber.setCellValueFactory(new PropertyValueFactory<>("articleNumber"));
                    column_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                    column_name.setCellValueFactory(new PropertyValueFactory<>("articleName"));

                    tableView_invoiceItem.setItems(observableList);
                });

                return null;
            }

        };

        new Thread(showAllTask).start();
    }


    private void executeAddItem() {
        // Es wurde mit absicht eine Map erzeugt, für den Fall das sich die Artikel Attributen in der Datenbank verändern.
        Map<String, String> articleToAdd = new HashMap<>();


        articleToAdd.put("article_id", field_articleNumber.getText());
        articleToAdd.put("amount", field_amount.getText());


        if (invoiceService.addItemToInvoiceWithInvoiceID(articleToAdd, invoice.getInvoiceID())) {
            clearFields();
        }
    }


    private void executeUpdateItem() {

    }


    private void executeDeleteItem() {

    }


    private void executeExit() {
        invoiceService.clearCurrentInvoice();
        SceneManager.switchScene("/com/example/styleslick/invoiceManagement-view.fxml", "Bestellung verwaltung", true);
    }






    @FXML
    private void onKeyPressedEnterAddItem(KeyEvent event) {
        executeAddItem();
    }

    @FXML
    private void onKeyPressedEnterDeleteItem(KeyEvent event) {

    }

    @FXML
    private void onKeyPressedEnterExit(KeyEvent event) {
        executeExit();
    }

    @FXML
    private void onKeyPressedEnterUpdateItem(KeyEvent event) {

    }

    @FXML
    private void onMouseClickedAddItem(MouseEvent event) {
        executeAddItem();
    }

    @FXML
    private void onMouseClickedDeleteItem(MouseEvent event) {

    }

    @FXML
    private void onMouseClickedExit(MouseEvent event) {
        executeExit();
    }

    @FXML
    private void onMouseClickedUpdateItem(MouseEvent event) {

    }


    private void clearFields() {
        field_amount.clear();
        field_articleNumber.clear();
    }
}
