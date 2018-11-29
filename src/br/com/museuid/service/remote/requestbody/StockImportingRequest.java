package br.com.museuid.service.remote.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.museuid.util.TimeUtils;

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
  @SerializedName("requesterid")
  @Expose
  private String requesterid;
  @SerializedName("requestername")
  @Expose
  private String requestername;
  @SerializedName("createdAt")
  @Expose
  private String createdAt;
  @SerializedName("updatedAt")
  @Expose
  private String updatedAt;



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
  public void reformatTime(){
    this.createdAt = TimeUtils.convertToLocalFormat(this.createdAt);
    this.updatedAt = TimeUtils.convertToLocalFormat(this.updatedAt);
  }
  public static class Item{
    private String itemid;
    private String itemname;
    private Integer quantity;

    public Item(String itemid, String itemname, Integer quantity) {
      this.itemid = itemid;
      this.itemname = itemname;
      this.quantity = quantity;
    }

    public String getItemid() {
      return itemid;
    }

    public void setItemid(String itemid) {
      this.itemid = itemid;
    }

    public String getItemname() {
      return itemname;
    }

    public void setItemname(String itemname) {
      this.itemname = itemname;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }
  }
}
