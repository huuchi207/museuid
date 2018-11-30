package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionUserInfo {
  @SerializedName("sessionid")
  @Expose
  private String sessionid;

  @SerializedName("token")
  @Expose
  private String token;

  @SerializedName("info")
  @Expose
  private UserDTO info;
  @SerializedName("haveSecureQuestion")
  @Expose
  private Boolean haveSecureQuestion;

  public Boolean getHaveSecureQuestion() {
    return haveSecureQuestion;
  }

  public void setHaveSecureQuestion(Boolean haveSecureQuestion) {
    this.haveSecureQuestion = haveSecureQuestion;
  }

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
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
