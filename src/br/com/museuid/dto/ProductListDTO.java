package br.com.museuid.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListDTO {
    @SerializedName("list")
    List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
