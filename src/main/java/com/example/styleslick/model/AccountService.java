package com.example.styleslick.model;


public class AccountService {

    private final Database database;


    public AccountService(Database database) {
        this.database = database;
    }



//    public boolean loginToDatabase(String username, String password) {
//        int userID;
//
//        if(!database.testConnection()) {
//            Rules.showErrorAlert("Die Verbindung zur Datenbank konnte nicht hergestellt werden.");
//            return false;
//        }
//        if(username.isEmpty() || password.isEmpty()) {
//            Rules.showErrorAlert("Bitte füllen Sie alle Felder aus.");
//            return false;
//        }
//        if(!database.usernameExists(username)) {
//            Rules.showErrorAlert("Benutzername existiert nicht.");
//            return false;
//        }
//
//        userID = database.getUserID(username, password);
//
//        if(userID == -1) {
//            Rules.showErrorAlert("Falsches Passwort.");
//            return false;
//        }
//
//        // Wenn der Login erfolgreich war, bekommt die Sitzung den Benutzer
//        UserSession session = UserSession.getInstance();
//        session
//                .setUserID(userID);
//        session.setName(database.getName(userID));
//
//        return true;
//    }


//    public boolean signUpToDatabase(String username, String password, String name) {
//
//        if(!database.testConnection()) {
//            Rules.showErrorAlert("Die Verbindung zur Datenbank konnte nicht hergestellt werden.");
//            return false;
//        }
//        if(username.isEmpty() || password.isEmpty() || name.isEmpty()) {
//            Rules.showErrorAlert("Bitte füllen Sie alle Felder aus.");
//            return false;
//        }
//        if(!Rules.isUsernameValid(username)) {
//            Rules.showErrorAlert("Der Benutzername darf nur Buchstaben, Zahlen enthalten und mindestens 4 Zeichen lang sein.");
//            return false;
//        }
//        if(database.usernameExists(username)) {
//            Rules.showErrorAlert("Benutzername ist schon vergeben.");
//            return false;
//        }
//        if(!Rules.isPasswordValid(password)) {
//            Rules.showErrorAlert("Das Passwort muss mindestens 8 Zeichen lang sein, einen Großbuchstaben, eine Zahl und ein Sonderzeichen enthalten.");
//            return false;
//        }
//        if(!Rules.isNameValid(name)) {
//            Rules.showErrorAlert("Der Name darf nur Buchstaben enthalten und nicht Leer sein.");
//            return false;
//        }
//
//        database.createAccount(name, username, password);
//        Rules.showConfirmAlert("Sie haben sich erfolgreich Registriert.");
//
//        return true;
//    }


//    public boolean addBookToDatabase(String title, String author, int yearOfPublication) {
//
//        UserSession session = UserSession.getInstance();
//        int userID = session.getUserID();
//
//        if(!Rules.isValidTitle(title)) {
//            Rules.showErrorAlert("Titel darf nicht leer sein und darf nur Buchstaben, Ziffern und Leerzeichen enthalten.");
//            return false;
//        }
//        if(!Rules.isValidAuthor(author)) {
//            Rules.showErrorAlert("Autor darf nicht leer sein und darf nur Buchstaben und Leerzeichen enthalten.");
//            return false;
//        }
//        if(!Rules.isValidYear(yearOfPublication)) {
//            Rules.showErrorAlert("Das Veröffentlichungsjahr ist falsch. Bitte geben Sie ein gültiges Jahr ein.");
//            return false;
//        }
//
//        database.addBook(title, author, yearOfPublication, userID);
//        Rules.showConfirmAlert("Das Buch wurde erfolgreich hinzugefügt.");
//
//        return true;
//    }


//    public boolean removeBookFromDatabase(String title) {
//        UserSession session = UserSession.getInstance();
//        int userID = session.getUserID();
//
//        if(!Rules.isValidTitle(title)) {
//            Rules.showErrorAlert("Titel darf nicht leer sein und darf nur Buchstaben, Ziffern und Leerzeichen enthalten.");
//            return false;
//        }
//        if(!database.bookExists(title, userID)) {
//            Rules.showErrorAlert("Das Buch existiert nicht.");
//            return false;
//        }
//
//        if(database.removeBook(title, userID)) {
//            Rules.showConfirmAlert("Das Buch wurde erfolgreich gelöscht.");
//            return true;
//        }
//
//        return false;
//    }





}
