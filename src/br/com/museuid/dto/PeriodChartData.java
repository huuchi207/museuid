package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PeriodChartData {
    @SerializedName("chartName")
    @Expose
    private String chartName;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("groupColumns")
    @Expose
    private List<GroupColumn> groupColumns = null;

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

    public List<GroupColumn> getGroupColumns() {
        return groupColumns;
    }

    public void setGroupColumns(List<GroupColumn> groupColumns) {
        this.groupColumns = groupColumns;
    }

}
