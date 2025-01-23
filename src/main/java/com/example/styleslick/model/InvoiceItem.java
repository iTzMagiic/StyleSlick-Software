package com.example.styleslick.model;

public class InvoiceItem {

    private final int articleID;
    private final String articleName;
    private final int amount;



    public InvoiceItem(int articleID, int amount, String articleName) {
        this.articleID = articleID;
        this.amount = amount;
        this.articleName = articleName;
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
}
