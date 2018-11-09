package customview.sectiongridview;

import java.util.List;

import br.com.museuid.model.data.Order;

public class ItemGridView {
    private String deviceName;
    private String macAddress;
    private List<Order> newOrder;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<Order> getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(List<Order> newOrder) {
        this.newOrder = newOrder;
    }
}
