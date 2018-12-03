package br.com.museuid.dto;

import com.google.gson.annotations.SerializedName;

public class UploadImageDTO {
  @SerializedName("_id")
  private String _id;

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }
}
