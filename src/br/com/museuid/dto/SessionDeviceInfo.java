package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionDeviceInfo {

    @SerializedName("sessionid")
    @Expose
    private String sessionid;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("info")
    @Expose
    private DeviceInfo info;

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public DeviceInfo getInfo() {
        return info;
    }

    public void setInfo(DeviceInfo info) {
        this.info = info;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
