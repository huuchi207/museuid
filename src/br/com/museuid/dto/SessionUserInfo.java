package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionUserInfo {
    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("info")
    @Expose
    private UserDTO info;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserDTO getInfo() {
        return info;
    }

    public void setInfo(UserDTO info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
