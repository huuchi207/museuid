package br.com.museuid.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductListDTO {
    @SerializedName("list")
    List<ProductWithImage> products;

    public List<ProductWithImage> getProducts() {
        return products;
    }

    public void setProducts(List<ProductWithImage> products) {
        this.products = products;
    }
}
