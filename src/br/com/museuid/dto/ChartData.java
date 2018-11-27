package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChartData {
    @SerializedName("chartName")
    @Expose
    private String chartName;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("groupChartPoints")
    @Expose
    private List<GroupChartPoint> groupChartPoints = null;

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<GroupChartPoint> getGroupChartPoints() {
        return groupChartPoints;
    }

    public void setGroupChartPoints(List<GroupChartPoint> groupChartPoints) {
        this.groupChartPoints = groupChartPoints;
    }

}
