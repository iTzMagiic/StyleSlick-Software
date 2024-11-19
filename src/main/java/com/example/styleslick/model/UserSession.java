package com.example.styleslick.model;

import java.util.List;

public class UserSession {

    /*
     * Diese Klasse implementiert das "Singleton-Pattern".
     * Ein Objekt der Klasse kann nur über die Methode getInstance() erstellt werden.
     * Dadurch wird sichergestellt, dass es in der gesamten Anwendung
     * nur eine einzige Instanz der Klasse gibt.
     */


    private static UserSession instance;
    private Database database;
    private List<Book> books;


    private UserSession() {}

    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void clearSession() {
        database = null;
        clearBooks();
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void clearBooks() {
        books = null;
    }

}
