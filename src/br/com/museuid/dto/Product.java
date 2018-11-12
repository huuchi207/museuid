package br.com.museuid.dto;

import com.google.gson.annotations.SerializedName;

import br.com.museuid.model.data.ProductInOrder;
import br.com.museuid.util.BundleUtils;

public class Product {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String productName;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private String price;
    @SerializedName("instock")
    private Integer inStock;
    private String status;
    public Product(String id, String productName, String description, String price, Integer inStock) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.inStock = inStock;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public ProductInOrder convertToProductInOrder(){
        return new ProductInOrder(id, productName, description, price, inStock, 1);
    }

    public void updateStatus(){
        this.status = (inStock!= null && inStock > 0) ?
            BundleUtils.getResourceBundle().getString("txt_available_product") :
            BundleUtils.getResourceBundle().getString("txt_out_of_stock");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
