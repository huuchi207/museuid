package br.com.museuid.model;

import br.com.museuid.dto.Product;

public class ProductInOrder extends Product {
    public ProductInOrder(String id, String productName, String description, int price, String status) {
        super(id, productName, description, price, status);
    }

    public ProductInOrder(String id, String productName, String description, int price, String status, int count) {
        super(id, productName, description, price, status);
        this.count = count;
        this.countString = String.valueOf(count);
    }
    private int count;
    private String countString;
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.countString = String.valueOf(count);
    }

    public String getCountString() {
        return countString;
    }

    public void setCountString(String countString) {
        this.countString = countString;
    }
}
