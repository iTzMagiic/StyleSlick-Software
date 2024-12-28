package com.example.styleslick.model;

public class Category {

    private final String name;

    private final int ID;


    public Category(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
