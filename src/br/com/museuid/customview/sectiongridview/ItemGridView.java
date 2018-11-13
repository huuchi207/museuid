package br.com.museuid.customview.sectiongridview;

import java.util.List;

import br.com.museuid.model.data.OrderDetail;

public class ItemGridView {
    private String deviceName;
    private String macAddress;
    private List<OrderDetail> newOrder;

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

    public List<OrderDetail> getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(List<OrderDetail> newOrder) {
        this.newOrder = newOrder;
    }
}
