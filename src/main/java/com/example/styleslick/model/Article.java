package com.example.styleslick.model;

public class Article {

    private int article_id;
    private int category_id;
    private String name;
    private String farbe;
    private double price;
    private String kaufdatum;
    private String hersteller;
    private String gekauft_bei;
    private String verarbeitung;
    private int menge;

    public Article(int article_id, int category_id, String name, String farbe, double price, String kaufdatum, String hersteller, String gekauft_bei, String verarbeitung, int menge) {
        this.article_id = article_id;
        this.category_id = category_id;
        this.name = name;
        this.farbe = farbe;
        this.price = price;
        this.kaufdatum = kaufdatum;
        this.hersteller = hersteller;
        this.gekauft_bei = gekauft_bei;
        this.verarbeitung = verarbeitung;
        this.menge = menge;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setKaufdatum(String kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public void setGekauft_bei(String gekauft_bei) {
        this.gekauft_bei = gekauft_bei;
    }

    public void setVerarbeitung(String verarbeitung) {
        this.verarbeitung = verarbeitung;
    }

    public void setMenge(int menge) {
        this.menge = menge;
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

    public String getFarbe() {
        return farbe;
    }

    public double getPrice() {
        return price;
    }

    public String getKaufdatum() {
        return kaufdatum;
    }

    public String getHersteller() {
        return hersteller;
    }

    public String getGekauft_bei() {
        return gekauft_bei;
    }

    public String getVerarbeitung() {
        return verarbeitung;
    }

    public int getMenge() {
        return menge;
    }
}
