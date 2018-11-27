package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupChartPoint {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("styleId")
    @Expose
    private Object styleId;
    @SerializedName("chartPoints")
    @Expose
    private List<ChartPoint> chartPoints = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Object getStyleId() {
        return styleId;
    }

    public void setStyleId(Object styleId) {
        this.styleId = styleId;
    }

    public List<ChartPoint> getChartPoints() {
        return chartPoints;
    }

    public void setChartPoints(List<ChartPoint> chartPoints) {
        this.chartPoints = chartPoints;
    }

}