package com.aplusstory.fixme;

public class ChartInfo {

    public String percentData, locationName, timeData;
    public FootprintDataManager.FootPrintData footPrintData = null;

    public ChartInfo(String percentData, String locationName, String timeData){
        this(percentData, locationName, timeData, null);
    }

    public ChartInfo(String percentData, String locationName, String timeData, FootprintDataManager.FootPrintData ftd) {
        this.percentData = percentData;
        this.locationName = locationName;
        this.timeData = timeData;
        this.footPrintData = ftd;
    }
}
