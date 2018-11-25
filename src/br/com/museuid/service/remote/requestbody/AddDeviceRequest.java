package br.com.museuid.service.remote.requestbody;

public class AddDeviceRequest {
    private String macaddress;
    private String name;
    private String location;
    private String username;
    private String password;

    public AddDeviceRequest(String macaddress, String name, String location, String username, String password) {
        this.macaddress = macaddress;
        this.name = name;
        this.location = location;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
