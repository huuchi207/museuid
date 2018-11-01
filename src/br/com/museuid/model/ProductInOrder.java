package br.com.museuid.model;

import br.com.museuid.dto.Product;

public class ProductInOrder extends Product {
    public ProductInOrder(String id, String productName, String description, int price, String status) {
        super(id, productName, description, price, status);
    }

    public ProductInOrder(String id, String productName, String description, int price, String status, int count) {
        super(id, productName, description, price, status);
        this.count = count;
    }
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
