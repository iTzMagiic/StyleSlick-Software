package com.example.styleslick.model;

public class InvoiceItem {

    private final int articleID;
    private final String articleName;
    private final int amount;
    private final int invoice_item_id;
    private final String articleNumber;


    public InvoiceItem(int articleID, String articleNumber, int amount, String articleName, int invoice_item_id) {
        this.articleID = articleID;
        this.articleNumber = articleNumber;
        this.amount = amount;
        this.articleName = articleName;
        this.invoice_item_id = invoice_item_id;
    }


    public int getArticleID() {
        return articleID;
    }


    public int getAmount() {
        return amount;
    }


    public String getArticleName() {
        return articleName;
    }


    public int getInvoiceItemID() {
        return invoice_item_id;
    }

    public String getArticleNumber() {
        return articleNumber;
    }
}
