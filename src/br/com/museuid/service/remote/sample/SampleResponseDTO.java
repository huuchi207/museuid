package br.com.museuid.service.remote.sample;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SampleResponseDTO<T> {

  @SerializedName("cod")
  @Expose
  private Long cod;
  @SerializedName("calctime")
  @Expose
  private Double calctime;
  @SerializedName("cnt")
  @Expose
  private Long cnt;
  @SerializedName("list")
  @Expose
  private java.util.List<T> list = null;

  public Long getCod() {
    return cod;
  }

  public void setCod(Long cod) {
    this.cod = cod;
  }

  public Double getCalctime() {
    return calctime;
  }

  public void setCalctime(Double calctime) {
    this.calctime = calctime;
  }

  public Long getCnt() {
    return cnt;
  }

  public void setCnt(Long cnt) {
    this.cnt = cnt;
  }

  public java.util.List<T> getList() {
    return list;
  }

  public void setList(java.util.List<T> list) {
    this.list = list;
  }

}