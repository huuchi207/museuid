package br.com.museuid.service.remote.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockImportingRequest {
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("pendingproductid")
  @Expose
  private String pendingproductid;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("decision")
  @Expose
  private String decision;
  @SerializedName("items")
  @Expose
  private List<Item> items;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Item> getItems() {
    return items;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getDecision() {
    return decision;
  }

  public void setDecision(String decision) {
    this.decision = decision;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public String getPendingproductid() {
    return pendingproductid;
  }

  public void setPendingproductid(String pendingproductid) {
    this.pendingproductid = pendingproductid;
  }

  public static class Item{
    private String productid;
    private String productname;
    private Integer quantity;

    public Item(String productid, String productname, Integer quantity) {
      this.productid = productid;
      this.productname = productname;
      this.quantity = quantity;
    }

    public String getProductid() {
      return productid;
    }

    public void setProductid(String productid) {
      this.productid = productid;
    }

    public String getProductname() {
      return productname;
    }

    public void setProductname(String productname) {
      this.productname = productname;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }
  }
}
