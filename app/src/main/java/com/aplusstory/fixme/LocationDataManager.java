package com.aplusstory.fixme;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public interface LocationDataManager {
    void setFileManager(FileManager fm);

    public class LocationData implements Serializable{
        public static final String KEY_DATETIME = "datetime";
        public static final String KEY_LATITUDE = "latitude";
        public static final String KEY_LONGITUDE = "longitude";
        public static final String DATE_FORMAT_GMT = "yyyy-MM-dd HH:mm:ss";
        public static final double T_RAD = 6378137.0D; //by meter

        long datetime; // in milliseconds
        double latitude;
        double longitude;

        @Nullable
        public static LocationData parseJSON(JSONObject json){
            DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT, Locale.US);
            Date date = null;
            long datetime = -1;
            double latitude = 0.0;
            double longtitude = 0.0;
            String dateTimeStr, latitudeStr,LongtitudeStr;

            try {
               dateTimeStr = json.getString(KEY_DATETIME);
               latitude = json.getDouble(KEY_LATITUDE);
               longtitude = json.getDouble(KEY_LONGITUDE);
               date = df.parse(dateTimeStr);
               datetime = date.getTime();
            } catch(JSONException e){
                return null;
            } catch (ParseException e){
                return null;
            }
            return new LocationData(datetime, latitude, longtitude);
        }

        public LocationData(String datetime, double latitude, double longitude){
            DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT, Locale.US);
            Date date = null;
            try {
                date = df.parse(datetime);
            } catch (ParseException e){
                //error handle
            }

            this.datetime = (date != null) ? date.getTime() : -1;

            this.latitude = latitude;
            this.longitude = longitude;
        }

        public LocationData(long datetime, double latitude, double longitude){
            this.datetime = datetime;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public JSONObject JSONify(){
            JSONObject json = new JSONObject();
            try {
                Date datetime = new Date(this.datetime);
                DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT, Locale.US);
                String dateStr = df.format(datetime);
                json.put(KEY_DATETIME, dateStr);
                json.put(KEY_LATITUDE, this.latitude);
                json.put(KEY_LONGITUDE, this.longitude);

            } catch (JSONException e){
                //error handle
            }

            return json;
        }

        @Override
        public String toString() {
            return this.JSONify().toString();
        }

        public static double distance(LocationData src, LocationData dst){
            double degToRad = Math.PI/180.0;
            double dlat = (src.latitude - dst.latitude) * degToRad;
            double dlong = (src.longitude - dst.longitude) * degToRad;

            double a = Math.pow(Math.sin(dlat/2.0), 2.0)
                    + (Math.cos(src.latitude * degToRad)
                        * Math.cos(dst.latitude * degToRad)
                        * Math.pow(Math.sin(dlong/2.0),2.0));

            return 2.0 * LocationData.T_RAD * Math.atan2(
                    Math.sqrt(a), Math.sqrt(1.0 - a)
            );
        }

        public double distanceTo(LocationData dst){
            return LocationData.distance(this, dst);
        }
    }

    class PathData implements Serializable{
        public static final String KEY_DATETIME_BEGIN = "dtBegin";
        public static final String KEY_DATETIME_END = "dtEnd";
        public static final String KEY_LOCATION_ARRAY = "locaArr";
        public static final String DATE_FORMAT_GMT = "yyyy-MM-dd HH:mm:ss";

        long dtBegin;
        long dtEnd;
        LocationData[] locaArr;

        public PathData(String dtBegin, String dtEnd, @NotNull LocationData[] locaArr) {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT, Locale.US);
            Date dBegin = null, dEnd = null;
            try{
                dBegin = df.parse(dtBegin);
                dEnd = df.parse(dtEnd);
            } catch (ParseException e){
                //error handle
            }

            this.dtBegin = (dBegin != null) ? dBegin.getTime() : -1;
            this.dtEnd = (dEnd != null) ? dEnd.getTime() : -1;
            this.locaArr = locaArr.clone();
        }

        public PathData(@NotNull LocationData[] locaArr){
            this.dtBegin = locaArr[0].datetime;
            this.dtEnd = locaArr[locaArr.length - 1].datetime;
            this.locaArr = locaArr.clone();
        }

        public PathData(@NotNull ArrayList<LocationData> locaList){
            this.locaArr = new LocationData[locaList.size()];
            this.locaArr = locaList.toArray(this.locaArr);
            this.dtBegin = this.locaArr[0].datetime;
            this.dtEnd = this.locaArr[this.locaArr.length -1].datetime;
        }

        public JSONObject JSONify(){
            JSONObject json = new JSONObject();
            JSONArray jsonPath = new JSONArray();
            try{
                Date dtBegin = new Date(this.dtBegin);
                Date dtEnd = new Date(this.dtEnd);
                DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT, Locale.US);
                json.put(KEY_DATETIME_BEGIN, df.format(dtBegin));
                json.put(KEY_DATETIME_END, df.format(dtEnd));
                for(LocationData loca : this.locaArr){
                    jsonPath.put(loca.JSONify());
                }
                json.put(KEY_LOCATION_ARRAY, jsonPath);
            } catch(JSONException e){

            }

            return json;
        }

        public double distance(){
            double distance = 0.0;
            if(this.locaArr != null){
                LocationData loca = this.locaArr[0];

                for(int i = 1; i < locaArr.length; i++){
                    distance += loca.distanceTo(locaArr[i]);
                    loca = locaArr[i];
                }
            }

            return distance;
        }

        @Override
        public String toString() {
            return this.JSONify().toString();
        }
    }
}
