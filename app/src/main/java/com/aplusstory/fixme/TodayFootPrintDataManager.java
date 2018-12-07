package com.aplusstory.fixme;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

public class TodayFootPrintDataManager implements FootprintDataManager {
    public interface  LocationNamer{
        String getName(LocationDataManager.LocationData location);
        String getName(LocationDataManager.PathData path);
    }

    public static final double DISTANCE_THRESHOLD = CurrentLocationManager.DISTANCE_THRESHOLD;
    public static final double PATH_THRESHOLD = CurrentLocationManager.DISTANCE_THRESHOLD * 2;
    public static final long INTERVAL_THRESHOLD = 5 * 60 * 1000;

    private LocationFileManager fm = null;
    private Context context;
    private Calendar today;
    private ArrayList<FootPrintData> dataArr = null;
    private LocationNamer namer = null;

    public static boolean isAvailableOnDate(Context context, Date date){
        boolean rt = false;
        File f = new File(LocationFileManager.getFilename(context, date));
        if(f.exists() && f.isFile()){
            rt = true;
        }

        return rt;
    }

    public static boolean isAvailableToday(Context context){
        return TodayFootPrintDataManager.isAvailableOnDate(context, new Date(System.currentTimeMillis()));
    }

    TodayFootPrintDataManager(Context context){
        this(context, new Date(System.currentTimeMillis()));
    }

    TodayFootPrintDataManager(Context context, Date today){
        this.context = context;
        this.fm = new LocationFileManager(context, LocationFileManager.READ_ONLY);
        this.today = Calendar.getInstance();
        this.today.setTime(today);
    }

    public boolean isAvailableToday(){
        return TodayFootPrintDataManager.isAvailableOnDate(this.context, this.today.getTime());
    }

    private void getDataForToday() throws NullPointerException{
        if(this.dataArr == null){
            this.dataArr = new ArrayList<>();
        } else{
            this.dataArr.clear();
        }

        LocationDataManager.LocationData[] arr = this.fm.getLocationList(this.today.getTime()).locaArr;
        LocationDataManager.LocationData lastLoca = null;
        LocationDataManager.LocationData pathBeginLoca = null;
        LocationDataManager.LocationData pathEndLoca = null;
        ArrayList<LocationDataManager.LocationData> bufPath = null;
        LocationDataManager.PathData path = null;
        FootPrintData pointData = null;
        FootPrintData pathData = null;
        double pathDistance = -1.0;
        long tBegin = 0, tEnd = 0;

        for(LocationDataManager.LocationData loca : arr){
            if(lastLoca == null && bufPath == null){
                lastLoca = loca;
                tBegin = loca.datetime;
                tEnd = loca.datetime;
                Log.d(this.getClass().getName(), "location : " + loca.toString());
            } else if(bufPath == null){
                Log.d(this.getClass().getName(),
                        "location : " + loca.toString()
                            + ", distance : " + lastLoca.distanceTo(loca));
                if(loca.distanceTo(lastLoca) > DISTANCE_THRESHOLD){
                    tEnd = loca.datetime;
                    if(pointData == null) {
                        pointData = new FootPrintData(tBegin, tEnd, lastLoca);
                        if(this.namer != null){
                            pointData.name = this.namer.getName(lastLoca);
                        }
                    }else {
                        pointData.dtEnd = tEnd;
                    }

                    Log.d(this.getClass().getName(), "footprint data : " + pointData.toString());

                    bufPath = new ArrayList<>();
                    bufPath.add(loca);
                    pathBeginLoca = loca;
                    pathEndLoca =loca;

                    Log.d(this.getClass().getName(), "path begin on : " + pathBeginLoca.toString());
                } else {
                    tEnd = loca.datetime;
                    Log.d(this.getClass().getName(), "nearby location");
                }
            } else {
                Log.d(this.getClass().getName(),
                        "path location : " + loca.toString()
                                +   ", distance : " + pathEndLoca.distanceTo(loca));
                if(loca.datetime - pathEndLoca.datetime >= INTERVAL_THRESHOLD
                        && loca.distanceTo(pathEndLoca) <= DISTANCE_THRESHOLD){

                    Log.d(this.getClass().getName(), "path ends on : " + pathEndLoca.toString());
                    path = new LocationDataManager.PathData(bufPath);
                    pathDistance = path.distance();
                    Log.d(this.getClass().getName(), "path length " + path.distance());
                    if(pathDistance >= PATH_THRESHOLD) {
                        pathData = new FootPrintData(pointData.dtEnd, loca.datetime, path);
                        Log.d(this.getClass().getName(), "footprint data : " + pathData.toString());
                        if(this.namer != null){
                            pathData.name = this.namer.getName(path);
                        }
                        this.dataArr.add(pointData);
                        pointData = null;
                        this.dataArr.add(pathData);
                        lastLoca = loca;
                    } else {
                        Log.d(this.getClass().getName(), "path too short");
                    }
                    bufPath = null;
                    tBegin = loca.datetime;
                    tEnd = loca.datetime;
                }else if(loca.distanceTo(pathEndLoca) >= DISTANCE_THRESHOLD){
                    bufPath.add(loca);
//                    Log.d(this.getClass().getName(),
//                            "path location : " + loca.toString()
//                            +   ", distance : " +pathEnd.distanceTo(loca));
                    pathEndLoca = loca;

                }else{
                    Log.d(this.getClass().getName(), "nearby location");
                }

//                if(loca.datetime - lastLoca.datetime < INTERVAL_THRESHOLD
//                || loca.distanceTo(lastLoca) >= DISTANCE_THRESHOLD){
//                    tEnd = loca.datetime;
//                    lastLoca = loca;
//                    Log.d(this.getClass().getName(), "path location : " + lastLoca.toString());
//                } else{
//                    path = new LocationDataManager.PathData(bufPath);
//                    data = new FootPrintData(path);
//                    if(this.namer != null){
//                        data.name = this.namer.getName(path);
//                    }
//                    this.dataArr.add(data);
//                    bufPath = null;
//                    lastLoca = loca;
//                    tBegin = loca.datetime;
//                    tEnd = loca.datetime;
//                    Log.d(this.getClass().getName(), "path ends on : " + loca.toString());
//                }
            }
        }

        long now = System.currentTimeMillis();
        if(now - this.today.getTimeInMillis() > 24 * 60 * 60 * 1000){
            Calendar c = (Calendar) this.today.clone();
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            now = c.getTimeInMillis();
        }


        if(bufPath != null){
            path = new LocationDataManager.PathData(bufPath);
            pathDistance = path.distance();
            Log.d(this.getClass().getName(), "path ends on : " + pathEndLoca.toString());
            Log.d(this.getClass().getName(), "path length " + path.distance());
            if(pathDistance >= PATH_THRESHOLD) {
                pathData = new FootPrintData(pointData.dtEnd, now, path);
                Log.d(this.getClass().getName(), "footprint data : " + pathData.toString());
                if(this.namer != null){
                    pathData.name = this.namer.getName(path);
                }
                this.dataArr.add(pointData);
                pointData = null;
                this.dataArr.add(pathData);
                lastLoca = null;
            } else {
                Log.d(this.getClass().getName(), "path too short");
                tEnd = lastLoca.datetime;
                bufPath = null;
            }
        }

        if(bufPath == null && (lastLoca != null || pointData != null)){
            if(lastLoca != null && pointData == null) {
                pointData = new FootPrintData(lastLoca.datetime, now, lastLoca);
                if (this.namer != null) {
                    pointData.name = this.namer.getName(lastLoca);
                }
            }
            this.dataArr.add(pointData);
            Log.d(this.getClass().getName(), "footprint data : " + pointData.toString());
        }
    }

    private LocationDataManager.LocationData getLastLocation(){
        if(this.dataArr != null) {
            Serializable lastData = this.dataArr.get(this.dataArr.size() - 1).locaData;
            if(lastData instanceof LocationDataManager.LocationData){
                return (LocationDataManager.LocationData)lastData;
            } else if(lastData instanceof LocationDataManager.PathData){
                LocationDataManager.LocationData[] arr = ((LocationDataManager.PathData)lastData).locaArr.clone();
                if(arr != null) {
                    return arr[arr.length - 1];
                }
            }
        }

        return null;
    }

    public ArrayList<FootPrintData> getData(){
        Calendar calNow = Calendar.getInstance();
        calNow.setTime(new Date(System.currentTimeMillis()));
        if((this.dataArr == null)
        || (this.today.get(Calendar.MONTH) != calNow.get(Calendar.MONTH))
        || (this.today.get(Calendar.DAY_OF_MONTH) != calNow.get(Calendar.DAY_OF_MONTH))
        || this.getLastLocation().distanceTo(this.fm.getCurrentLocation()) < DISTANCE_THRESHOLD){
            try {
                this.getDataForToday();
            }catch(Exception e){
                Log.d(this.getClass().getName(), e.toString());
                for(StackTraceElement ste : e.getStackTrace()){
                    Log.d(this.getClass().getName(), ste.toString());
                }
            }
        }

        if(this.dataArr == null){
            Log.d(this.getClass().getName(), "null?!");
        }

        return new ArrayList<FootPrintData>(this.dataArr);
    }


    @Override
    public void setFileManager(FileManager f) {
        if(f instanceof LocationFileManager){
            this.fm = (LocationFileManager)f;
        }
    }

    public void setNamer(LocationNamer namer){
        this.namer = namer;
    }


}
