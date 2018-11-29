package br.com.museuid.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChartData {
  @SerializedName("chartName")
  @Expose
  private String chartName;
  @SerializedName("xAxisUnit")
  @Expose
  private String xAxisUnit;
  @SerializedName("yAxisUnit")
  @Expose
  private String yAxisUnit;
  @SerializedName("groupChartPoints")
  @Expose
  private List<GroupChartPoint> groupChartPoints = null;

  public String getChartName() {
    return chartName;
  }

  public void setChartName(String chartName) {
    this.chartName = chartName;
  }

  public String getxAxisUnit() {
    return xAxisUnit;
  }

  public void setxAxisUnit(String xAxisUnit) {
    this.xAxisUnit = xAxisUnit;
  }

  public List<GroupChartPoint> getGroupChartPoints() {
    return groupChartPoints;
  }

  public void setGroupChartPoints(List<GroupChartPoint> groupChartPoints) {
    this.groupChartPoints = groupChartPoints;
  }

  public String getyAxisUnit() {
    return yAxisUnit;
  }

  public void setyAxisUnit(String yAxisUnit) {
    this.yAxisUnit = yAxisUnit;
  }
}
