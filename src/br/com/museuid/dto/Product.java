package br.com.museuid.dto;

import br.com.museuid.model.data.ProductInOrder;

public class Product {
    private String id;
    private String productName;
    private String description;
    private int price;
    private String status;

    public Product(String id, String productName, String description, int price, String status) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductInOrder convertToProductInOrder(){
        return new ProductInOrder(id, productName,description, price, status, 0);
    }
}
