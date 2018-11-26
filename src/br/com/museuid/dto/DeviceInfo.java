package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

public class DeviceInfo implements Comparable<DeviceInfo>{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("macaddress")
    @Expose
    private String macaddress;
    @SerializedName("update")
    @Expose
    private String update;
    private String clientid;
    private String displayName;
    public DeviceInfo() {
    }

    public DeviceInfo(String id, String name, String location, String macaddress) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.macaddress = macaddress;
        this.clientid = id;
    }

    public DeviceInfo(String name, String location, String macaddress) {
        this.name = name;
        this.location = location;
        this.macaddress = macaddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public void buildDisplayName(){
        StringBuilder s = new StringBuilder(this.name);
        if (!StringUtils.isEmpty(this.location)){
            s.append(" - ");
            s.append(this.location);
        }
        this.displayName = s.toString();
    }

    @Override
    public String toString() {//for auto complete text
        return displayName;
    }

    @Override
    public int compareTo(DeviceInfo o) {
        if (this.displayName == null || o.displayName == null)
            return 0;
        return this.displayName.compareTo(o.displayName);
    }
}