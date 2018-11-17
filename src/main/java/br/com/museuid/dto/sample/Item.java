package br.com.museuid.dto.sample;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

  @SerializedName("main")
  @Expose
  private Main main;

  public Main getMain() {
    return main;
  }

  public void setMain(Main main) {
    this.main = main;
  }

}