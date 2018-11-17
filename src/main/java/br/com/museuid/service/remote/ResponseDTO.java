package br.com.museuid.service.remote;

import com.google.gson.annotations.SerializedName;

public class ResponseDTO<T> {
  @SerializedName("isError")
  private Boolean error;

  @SerializedName("message")
  private String message;

  @SerializedName("result")
  private T result;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}