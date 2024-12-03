package com.example.styleslick.service;

import com.example.styleslick.model.Database;

public class OrderService {

    private static OrderService orderService;
    private Database database;



    private OrderService() {}



    public static OrderService getInstance() {
        if (orderService == null) {
            orderService = new OrderService();
        }
        return orderService;
    }


    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }


    public void clearSession() {
        database = null;
        orderService = null;
    }
}
