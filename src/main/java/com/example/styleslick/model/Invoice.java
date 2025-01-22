package com.example.styleslick.model;

import java.time.LocalDate;

public class Invoice {

    private final int invoiceID;
    private final int customerID;
    private final String customer_number;
    private final String invoice_number;
    private final LocalDate purchase_date;
    private final String payment_method;
    private final String transaction_number;
    private final double payment_amount;
    private final double shipping_cost;
    private final String shipping_receipt;
    private final String shipping_method;

    public Invoice(String customer_number, int invoiceID, int customerID, String invoice_number, LocalDate purchase_date, String payment_method,
                   String transaction_number, double payment_amount, double shipping_cost, String shipping_receipt,
                   String shipping_method) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.customer_number = customer_number;
        this.invoice_number = invoice_number;
        this.purchase_date = purchase_date;
        this.payment_method = payment_method;
        this.transaction_number = transaction_number;
        this.payment_amount = payment_amount;
        this.shipping_cost = shipping_cost;
        this.shipping_receipt = shipping_receipt;
        this.shipping_method = shipping_method;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerNumber() {
        return customer_number;
    }

    public String getInvoiceNumber() {
        return invoice_number;
    }

    public LocalDate getPurchaseDate() {
        return purchase_date;
    }

    public String getPaymentMethod() {
        return payment_method;
    }

    public String getTransactionNumber() {
        return transaction_number;
    }

    public double getPaymentAmount() {
        return payment_amount;
    }

    public double getShippingCost() {
        return shipping_cost;
    }

    public String getShippingReceipt() {
        return shipping_receipt;
    }

    public String getShippingMethod() {
        return shipping_method;
    }
}
