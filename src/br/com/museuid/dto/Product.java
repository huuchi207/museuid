package br.com.museuid.dto;

import com.google.gson.annotations.SerializedName;

import br.com.museuid.model.data.ProductInOrder;
import br.com.museuid.util.BundleUtils;

public class Product {
  @SerializedName("_id")
  protected String id;
  @SerializedName("name")
  protected String productName;
  @SerializedName("description")
  protected String description;
  @SerializedName("price")
  protected Integer price;
  @SerializedName("instock")
  protected Integer inStock;
  protected String status;
  protected String productid;
  @SerializedName("imageid")
  protected String imageid;


  public Product(String id, String productName, String description, Integer price, Integer inStock) {
    this.id = id;
    this.productid = id;
    this.productName = productName;
    this.description = description;
    this.price = price;
    this.inStock = inStock;
  }

  public Product(String productName, String description, Integer price, Integer inStock) {
    this.productName = productName;
    this.description = description;
    this.price = price;
    this.inStock = inStock;
  }

  public Product(String productName, String productid) {
    this.productName = productName;
    this.productid = productid;
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

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getInStock() {
    return inStock;
  }

  public void setInStock(Integer inStock) {
    this.inStock = inStock;
  }

  public ProductInOrder convertToProductInOrder() {
    return new ProductInOrder(id, productName, description, price, inStock, 1);
  }

  public void updateStatus() {
    this.status = (inStock != null && inStock > 0) ?
      BundleUtils.getResourceBundle().getString("txt_available_product") :
      BundleUtils.getResourceBundle().getString("txt_out_of_stock");
  }

  public String getImageid() {
    return imageid;
  }

  public void setImageid(String imageid) {
    this.imageid = imageid;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getProductid() {
    return productid;
  }

  public void setProductid(String productid) {
    this.productid = productid;
  }

  public ProductWithImage convertToProductWithImage(){
    return new ProductWithImage(id, productName, description, price, inStock, imageid);
  }
}
