package com.example.styleslick.model;

import java.time.LocalDate;

public class Article {

    private int article_id;
    private int category_id;
    private String name;
    private String farbe;
    private double kaufpreis;
    private LocalDate kaufdatum;
    private String hersteller;
    private String gekauft_ueber;
    private String verarbeitung;
    private int menge;
    private int bestand;

    public Article(int article_id, int category_id, String name, String farbe, double kaufpreis, LocalDate kaufdatum, String hersteller, String gekauft_ueber, String verarbeitung, int menge, int bestand) {
        this.article_id = article_id;
        this.category_id = category_id;
        this.name = name;
        this.farbe = farbe;
        this.kaufpreis = kaufpreis;
        this.kaufdatum = kaufdatum;
        this.hersteller = hersteller;
        this.gekauft_ueber = gekauft_ueber;
        this.verarbeitung = verarbeitung;
        this.menge = menge;
        this.bestand = bestand;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    public int getBestand() {
        return bestand;
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

    public void setKaufpreis(double kaufpreis) {
        this.kaufpreis = kaufpreis;
    }

    public void setKaufdatum(LocalDate kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public void setGekauft_ueber(String gekauft_ueber) {
        this.gekauft_ueber = gekauft_ueber;
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

    public double getKaufpreis() {
        return kaufpreis;
    }

    public LocalDate getKaufdatum() {
        return kaufdatum;
    }

    public String getHersteller() {
        return hersteller;
    }

    public String getGekauft_ueber() {
        return gekauft_ueber;
    }

    public String getVerarbeitung() {
        return verarbeitung;
    }

    public int getMenge() {
        return menge;
    }
}
