package br.com.museuid.service.remote.requestbody;

public class StatisticRequest {
    private String startdate;
    private String enddate;

    public StatisticRequest(String startdate, String enddate) {
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
}
