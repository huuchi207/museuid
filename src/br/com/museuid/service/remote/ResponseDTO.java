package br.com.museuid.service.remote;

import com.google.gson.annotations.SerializedName;

public class ResponseDTO<T> {
  @SerializedName("errorCode")
  private String errorCode;

  @SerializedName("errorMessage")
  private String errorMessage;

  @SerializedName("content")
  private T content;

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }
}