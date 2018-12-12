package br.com.museuid.service.remote.requestbody;

public class StringBody {
  String key;

  public StringBody(String key) {
    this.key = key;
  }

  public StringBody() {
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
