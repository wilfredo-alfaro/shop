package com.telus.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Stock {

    private static Stock instance;

    private List<Item> items = new ArrayList<Item>();

    private Stock() {
    }

    public static Stock getInstance() {
        if (instance == null) {
            synchronized (Stock.class) {
                if (instance == null) {
                    instance = new Stock();
                }
            }
        }
        return instance;
    }

    public List<Item> getItems() {
        return items;
    }

    private void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "items=" + items +
                '}';
    }
}
