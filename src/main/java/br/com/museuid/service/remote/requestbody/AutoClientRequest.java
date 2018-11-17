package br.com.museuid.service.remote.requestbody;

public class AutoClientRequest {
    public AutoClientRequest(String macAddress) {
        this.macAddress = macAddress;
    }

    String macAddress;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
