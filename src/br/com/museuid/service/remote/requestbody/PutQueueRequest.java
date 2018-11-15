package br.com.museuid.service.remote.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PutQueueRequest {
    @SerializedName("sumup")
    @Expose
    protected Integer sumup;
    @SerializedName("items")
    @Expose
    protected List<Item> items = null;
    @SerializedName("customername")
    @Expose
    protected String customername;
    @SerializedName("customerid")
    @Expose
    protected String customerid;
    @SerializedName("sessionid")
    @Expose
    protected String sessionid;
    @SerializedName("comment")
    @Expose
    protected String comment;

    @SerializedName("location")
    @Expose
    protected String location;

    public PutQueueRequest(Integer sumup, List<Item> items, String customername, String customerid, String sessionid, String comment) {
        this.sumup = sumup;
        this.items = items;
        this.customername = customername;
        this.customerid = customerid;
        this.sessionid = sessionid;
        this.comment = comment;
    }

    public Integer getSumup() {
        return sumup;
    }

    public void setSumup(Integer sumup) {
        this.sumup = sumup;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "PutQueueRequest{" +
            "sumup='" + sumup + '\'' +
            ", items=" + items +
            ", customername='" + customername + '\'' +
            ", customerid='" + customerid + '\'' +
            ", sessionid='" + sessionid + '\'' +
            ", comment='" + comment + '\'' +
            '}';
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static class Item {
        @SerializedName("itemname")
        @Expose
        private String itemname;
        @SerializedName("itemid")
        @Expose
        private String itemid;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("inittotal")
        @Expose
        private Integer inittotal;
        @SerializedName("note")
        @Expose
        private String note;
        @SerializedName("total")
        @Expose
        private Integer total;

        public Item(String itemname, String itemid, Integer quantity, Integer price, Integer inittotal, String note, Integer total) {
            this.itemname = itemname;
            this.itemid = itemid;
            this.quantity = quantity;
            this.price = price;
            this.inittotal = inittotal;
            this.note = note;
            this.total = total;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getInittotal() {
            return inittotal;
        }

        public void setInittotal(Integer inittotal) {
            this.inittotal = inittotal;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Item{" +
                "itemname='" + itemname + '\'' +
                ", itemid='" + itemid + '\'' +
                ", quantity=" + quantity +
                ", price='" + price + '\'' +
                ", inittotal='" + inittotal + '\'' +
                ", note='" + note + '\'' +
                ", total='" + total + '\'' +
                '}';
        }
    }


}
