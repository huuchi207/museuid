package br.com.museuid.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseChartItem {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("value")
    @Expose
    private Double value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
