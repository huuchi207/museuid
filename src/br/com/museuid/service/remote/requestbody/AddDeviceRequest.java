package br.com.museuid.service.remote.requestbody;

public class AddDeviceRequest {
    String macaddress;
    String name;
    String location;

    public AddDeviceRequest(String macaddress, String name, String location) {
        this.macaddress = macaddress;
        this.name = name;
        this.location = location;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
