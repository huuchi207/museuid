package br.com.museuid.service.remote.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.EnumUtils;

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

  @SerializedName("status")
  @Expose
  private String status;
  private String statusText;

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public String getRequesterid() {
    return requesterid;
  }

  public void setRequesterid(String requesterid) {
    this.requesterid = requesterid;
  }

  public String getRequestername() {
    return requestername;
  }

  public void setRequestername(String requestername) {
    this.requestername = requestername;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

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

  private String content;
  private String number;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void updateFields(){
    reformatTime();
    StringBuilder content=new StringBuilder();
    for (Item item : items){
      content.append(item.getItemname());
      content.append(": ");
      content.append(item.getQuantity());
      content.append(",\n");
    }

    this.setContent(content.substring(0, content.length()-2));
    if (EnumUtils.isValidEnum(Status.class, status)){
      statusText = Status.valueOf(status).toString();
    }
  }

  public void reformatTime(){
    this.createdAt = TimeUtils.convertToLocalFormat(this.createdAt);
    this.updatedAt = TimeUtils.convertToLocalFormat(this.updatedAt);
  }

  public enum Status{
    NEW{
      @Override
      public String toString(){
        return "Chưa xử lý";
      }
    },
    APPROVED{
      @Override
      public String toString(){
        return "Được chấp nhận";
      }
    },
    REJECTED{
      @Override
      public String toString(){
        return "Bị từ chối";
      }
    },
    CANCELED{
      @Override
      public String toString(){
        return "Đã hủy";
      }
    }
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
