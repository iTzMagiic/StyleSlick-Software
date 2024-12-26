package com.example.styleslick.model;

import java.time.LocalDate;

public class Article {

    private final int article_id;
    private final int category_id;
    private final String name;
    private final String farbe;
    private final double kaufpreis;
    private final LocalDate kaufdatum;
    private final String hersteller;
    private final String gekauft_ueber;
    private final String verarbeitung;
    private final int menge;
    private final int bestand;

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

    public int getBestand() {
        return bestand;
    }
}
