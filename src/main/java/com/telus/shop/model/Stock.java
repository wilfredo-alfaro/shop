package com.telus.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Stock {

    private List<Item> items = new ArrayList<Item>();

    public Stock() {
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
