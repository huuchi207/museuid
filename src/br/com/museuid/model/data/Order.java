package br.com.museuid.model.data;

import java.util.List;

import br.com.museuid.util.BundleUtils;

public class Order {
    private List<ProductInOrder> productInOrders;
    private String status;
    private String orderStatusName;
    private String computerName;
    private String orderId;
    public List<ProductInOrder> getProductInOrders() {
        return productInOrders;
    }

    public void setProductInOrders(List<ProductInOrder> productInOrders) {
        this.productInOrders = productInOrders;

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
