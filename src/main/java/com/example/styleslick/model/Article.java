package com.example.styleslick.model;

import java.time.LocalDate;

public class Article {

    private final int article_id;
    private final int category_id;
    private final String name;
    private final String color;
    private final double purchase_price;
    private final LocalDate purchase_date;
    private final String manufacturer;
    private final String purchased_from;
    private final String quality;
    private final int amount;
    private final int stock;

    public Article(int article_id, int category_id, String name, String color, double purchase_price, LocalDate purchase_date, String manufacturer, String purchased_from, String quality, int amount, int stock) {
        this.article_id = article_id;
        this.category_id = category_id;
        this.name = name;
        this.color = color;
        this.purchase_price = purchase_price;
        this.purchase_date = purchase_date;
        this.manufacturer = manufacturer;
        this.purchased_from = purchased_from;
        this.quality = quality;
        this.amount = amount;
        this.stock = stock;
    }

    public int getArticle_id() {
        return article_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public double getPurchase_price() {
        return purchase_price;
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
