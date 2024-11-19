package com.example.styleslick.model;

import java.io.Serial;
import java.io.Serializable;

// Durch die Implementierung von "Serializable" ist es möglich die Klasse "Book" in Dateien zu speichern.
public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /*
        "serialVersionUID" ist eine konstante Versionskontrolle, wenn man die "serialVersionUID" nicht manuell angibt
        wird die vom Compiler automatisch erzeugt und bei jeder änderung in der Klasse, wird eine neue Version erzeugt
        und gibt ein Fehler aus, wenn die abweichend zu der alten in der Datei ist.
        "serialVersionUID" ist abhängig von der Implementierung der Klasse "Serializable"
     */
    private final String title;
    private final String author;
    private final int yearOfPublication;


    public Book(String title, String author, int yearOfPublication) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    @Override
    public String toString() {
        return "Titel: " + title + "\t\t Autor: " + author + "\t\t Erscheinungsjahr: " + yearOfPublication;
    }

}
