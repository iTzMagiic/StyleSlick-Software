package com.example.styleslick.model;

import java.time.LocalDate;

public class Article {

    private final int articleID;
    private final int categoryID;
    private final String articleNumber;
    private final String categoryName;
    private final String name;
    private final String color;
    private final double price;
    private final LocalDate purchase_date;
    private final String manufacturer;
    private final String purchased_from;
    private final String quality;
    private final int amount;
    private final int stock;

    public Article(int articleID, int categoryID, String articleNumber, String categoryName, String name, String color, double price, LocalDate purchase_date, String manufacturer, String purchased_from, String quality, int amount, int stock) {
        this.articleID = articleID;
        this.categoryID = categoryID;
        this.articleNumber = articleNumber;
        this.categoryName = categoryName;
        this.name = name;
        this.color = color;
        this.price = price;
        this.purchase_date = purchase_date;
        this.manufacturer = manufacturer;
        this.purchased_from = purchased_from;
        this.quality = quality;
        this.amount = amount;
        this.stock = stock;
    }

    public int getArticleID() {
        return articleID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getPurchase_date() {
        return purchase_date;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getPurchased_from() {
        return purchased_from;
    }

    public String getQuality() {
        return quality;
    }

    public int getAmount() {
        return amount;
    }

    public int getStock() {
        return stock;
    }
}
