package com.example.styleslick.service;

import com.example.styleslick.model.Database;

public class UserSession {
    /*
     * Diese Klasse implementiert das "Singleton-Pattern".
     * Ein Objekt der Klasse kann nur über die Methode getInstance() erstellt werden.
     * Dadurch wird sichergestellt, dass es in der gesamten Anwendung
     * nur eine einzige Instanz der Klasse gibt.
     */

    private static UserSession userSession;
    private Database database;


    private UserSession() {}


    public static UserSession getInstance() {
        if (userSession == null) {
            userSession = new UserSession();
        }
        return userSession;
    }

    public void clearSession() {
        database = null;
        userSession = null;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }


}
