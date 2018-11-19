package br.com.museuid.model.data;

import br.com.museuid.service.remote.requestbody.PutQueueRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.TimeUtils;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.EnumUtils;

import java.util.List;

public class OrderDetail extends PutQueueRequest {
    @SerializedName("_id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("update")
    private String update;
    @SerializedName("createdAt")
    private String createdAt;
    private String orderDescription;
    private String orderName;
    private String statusText;

    public String getQueueid() {
        return queueid;
    }

    public void setQueueid(String queueid) {
        this.queueid = queueid;
    }

    @SerializedName("queueid")
    private String queueid;

    public OrderDetail() {
        super();
    }

    public OrderDetail(Integer sumup, List<Item> items, String customername, String customerid, String sessionid, String comment) {
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
        this.queueid = id;
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
        this.createdAt = TimeUtils.convertToLocalFormat(createdAt);
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
