package com.aplusstory.fixme;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface FootprintDataManager extends UserDataManager{
    public static final String KEY_DATA = "footprint_data";

    @Override
    void setFileManager(FileManager f);



    public class FootPrintData implements Serializable{
        public static final String DATE_FORMAT_GMT = "yyyy-MM-dd HH:mm:ss";

        long dtBigin = -1;
        long dtEnd = -1;
        Class locaDataType;
        Serializable locaData;
        String name;

        public FootPrintData(long begin, long end, LocationDataManager.LocationData loca){
            this.dtBigin = begin;
            this.dtEnd = end;
            if(loca != null) {
                this.locaDataType = loca.getClass();
                this.locaData = (Serializable) loca;
            } else{
                this.locaData = null;
                this.locaDataType = null;
            }
        }

        public FootPrintData(Date begin, Date end, LocationDataManager.LocationData loca){
            this(begin.getTime(), end.getTime(), loca);
        }

        public FootPrintData(@NotNull LocationDataManager.PathData path){
            this(path.dtBegin, path.dtEnd, path);
        }
        public FootPrintData(long begin, long end, LocationDataManager.PathData path) {
            this.dtBigin = begin;
            this.dtEnd = end;
            if (path != null) {
                this.locaData = (Serializable) path;
                this.locaDataType = path.getClass();
            } else {
                this.locaData = null;
                this.locaDataType = null;
            }
        }
        public long getInterval(){
            return this.dtEnd - this.dtBigin;
        }

        public boolean isPath(){
            return this.locaData instanceof LocationDataManager.PathData;
        }

        public boolean isSingleCoordinate(){
            return this.locaData instanceof LocationDataManager.LocationData;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            DateFormat df = new SimpleDateFormat(FootPrintData.DATE_FORMAT_GMT);
            if(this.name != null) {
                sb.append(this.name);
            } else {
                sb.append("footprint data");
            }
            if(this.dtBigin >= 0 && this.dtEnd >= 0) {
                sb.append(", begin : " + df.format(new Date(this.dtBigin)));
                sb.append(", end : " + df.format(new Date(this.dtEnd)));
            }else {
                sb.append(", no time data");
            }
            if(this.locaDataType == LocationDataManager.LocationData.class){
                sb.append(", location : ");
                sb.append("{ latitude : " + ((LocationDataManager.LocationData)this.locaData).latitude);
                sb.append(", longitude : " + ((LocationDataManager.LocationData)this.locaData).longitude + " }");
            } else if(this.locaDataType == LocationDataManager.PathData.class){
                LocationDataManager.LocationData[] arr = ((LocationDataManager.PathData)this.locaData).locaArr;
                sb.append(", path begin : ");
                sb.append("{ latitude : " + (arr[0]).latitude);
                sb.append(", longitude : " + (arr[0]).longitude + " }");
                sb.append(", path end : ");
                sb.append("{ latitude : " + (arr[arr.length - 1]).latitude);
                sb.append(", longitude : " + (arr[arr.length - 1]).longitude + " }");
            } else{
                sb.append(", no location data");
            }

            return sb.toString();
        }
    }
}
