package com.aplusstory.fixme;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public interface LocationDataManager {
    void setFileManager(FileManager fm);

    public class LocatonData implements Serializable{
        public static final String KEY_DATETIME = "datetime";
        public static final String KEY_LATITUDE = "latitude";
        public static final String KEY_LONGITUDE = "longitude";
        public static final String DATE_FORMAT_GMT = "yyyy-MM-dd HH:mm:ss";
        public static final double T_RAD = 6378137.0D; //by meter

        long datetime; // in milliseconds
        double latitude;
        double longitude;

        @Nullable
        public static LocatonData parseJSON(JSONObject json){
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
            return new LocatonData(datetime, latitude, longtitude);
        }

        public LocatonData(String datetime, double latitude, double longitude){
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

        public LocatonData(long datetime, double latitude, double longitude){
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

        public static double distance(LocatonData src, LocatonData dst){
            double degToRad = Math.PI/180.0;
            double dlat = (src.latitude - dst.latitude) * degToRad;
            double dlong = (src.longitude - dst.longitude) * degToRad;

            double a = Math.pow(Math.sin(dlat/2.0), 2.0)
                    + (Math.cos(src.latitude * degToRad)
                        * Math.cos(dst.latitude * degToRad)
                        * Math.pow(Math.sin(dlong/2.0),2.0));

            return 2.0 * LocatonData.T_RAD * Math.atan2(
                    Math.sqrt(a), Math.sqrt(1.0 - a)
            );
        }

        public double distanceTo(LocationDataManager.LocatonData dst){
            return LocationDataManager.LocatonData.distance(this, dst);
        }
    }
    class PathData{
        public static final String KEY_DATETIME_BEGIN = "dtBegin";
        public static final String KEY_DATETIME_END = "dtEnd";
        public static final String KEY_LOCATION_ARRAY = "locaArr";
        public static final String DATE_FORMAT_GMT = "yyyy-MM-dd HH:mm:ss";

        long dtBegin;
        long dtEnd;
        LocatonData[] locaArr;

        public PathData(String dtBegin, String dtEnd, LocatonData[] locaArr) {
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

        public JSONObject JSONify(){
            JSONObject json = new JSONObject();
            JSONArray jsonPath = new JSONArray();
            try{
                Date dtBegin = new Date(this.dtBegin);
                Date dtEnd = new Date(this.dtEnd);
                DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT, Locale.US);
                json.put(KEY_DATETIME_BEGIN, df.format(dtBegin));
                json.put(KEY_DATETIME_END, df.format(dtEnd));
                for(LocationDataManager.LocatonData loca : this.locaArr){
                    jsonPath.put(loca);
                }
                json.put(KEY_LOCATION_ARRAY, jsonPath);
            } catch(JSONException e){

            }

            return json;
        }

        @Override
        public String toString() {
            return this.JSONify().toString();
        }
    }
}
