package br.com.museuid.model.data;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;

import br.com.museuid.service.remote.requestbody.PutQueueRequest;
import br.com.museuid.util.BundleUtils;

public class OrderDetail extends PutQueueRequest {
    @SerializedName("_id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("update")
    private String update;
    private String orderDescription;
    private String orderName;
    private String statusText;

    public OrderDetail(String sumup, List<Item> items, String customername, String customerid, String sessionid, String comment) {
        super(sumup, items, customername, customerid, sessionid, comment);
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void updateFields(){
        //build name, description of whole order
        StringBuilder orderName= new StringBuilder(), orderDescription=new StringBuilder();
        for (PutQueueRequest.Item item : items){
            orderName.append(item.getItemname());
            orderName.append(", ");
            orderDescription.append(item.getItemname());
            orderDescription.append(": ");
            orderDescription.append(item.getQuantity());
            orderDescription.append(",\n");
        }

        this.setOrderDescription(orderDescription.substring(0, orderDescription.length()-2));
        this.setOrderName(orderName.substring(0, orderName.length() -2));
        if (EnumUtils.isValidEnum(OrderStatus.class, status)){
            statusText = OrderStatus.valueOf(status).toString();
        }
    }
    public enum OrderStatus{
        NEW {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_unhandle_order");
            }
        },
        PROGRESSING {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_progressing_order");
            }
        },
        DONE {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_done_order");
            }
        },
        CANCELED {
            public String toString(){
                return BundleUtils.getResourceBundle().getString("txt_canceled_order");
            }
        }
        //example
//        if (EnumUtils.isValidEnum(OrderStatus.class, "status")){
//            String statusName = OrderStatus.valueOf("status").toString();
//        }
    }

}