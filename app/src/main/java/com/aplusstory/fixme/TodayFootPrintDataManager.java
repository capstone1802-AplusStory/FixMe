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
    public static final long INTERVAL_THRESHOLD = 10 * 60 * 1000;

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
        ArrayList<LocationDataManager.LocationData> bufPath = null;
        LocationDataManager.PathData path = null;
        FootPrintData data = null;

        long tBegin = 0, tEnd = 0;

        for(LocationDataManager.LocationData loca : arr){
            if(lastLoca == null && bufPath == null){
                lastLoca = loca;
                tBegin = loca.datetime;
                tEnd = loca.datetime;
                Log.d(this.getClass().getName(), "location : " + lastLoca.toString());
            } else if(bufPath == null){
                if(loca.distanceTo(lastLoca) > DISTANCE_THRESHOLD){
                    tEnd = loca.datetime;
                    data = new FootPrintData(tBegin, tEnd, lastLoca);
                    if(this.namer != null){
                        data.name = this.namer.getName(lastLoca);
                    }
                    this.dataArr.add(data);
                    bufPath = new ArrayList<>();
                    tBegin = tEnd;
                    bufPath.add(loca);
                    lastLoca = loca;
                    Log.d(this.getClass().getName(), "path begin on : " + loca.toString());
                } else {
                    tEnd = loca.datetime;
                    Log.d(this.getClass().getName(), "nearby location : " + lastLoca.toString());
                }
            } else {
                if(loca.datetime - lastLoca.datetime >= INTERVAL_THRESHOLD
                        && loca.distanceTo(lastLoca) >= DISTANCE_THRESHOLD){
                    path = new LocationDataManager.PathData(bufPath);
                    data = new FootPrintData(tBegin, tEnd, path);
                    if(this.namer != null){
                        data.name = this.namer.getName(path);
                    }
                    this.dataArr.add(data);
                    bufPath = null;
                    lastLoca = loca;
                    tBegin = tEnd;
                    tEnd = loca.datetime;
                    Log.d(this.getClass().getName(), "path ends on : " + loca.toString());
                }else if(loca.distanceTo(lastLoca) >= DISTANCE_THRESHOLD){
                    bufPath.add(loca);
                    tEnd = loca.datetime;
                    lastLoca = loca;
                    Log.d(this.getClass().getName(), "path location : " + lastLoca.toString());
                }else{
                    tEnd = loca.datetime;
                    Log.d(this.getClass().getName(), "nearby location : " + lastLoca.toString());
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

        if(bufPath != null){
            path = new LocationDataManager.PathData(bufPath);
            data = new FootPrintData(path);
            if(this.namer != null){
                data.name = this.namer.getName(path);
            }
            this.dataArr.add(data);
        }else if(lastLoca != null){
            data = new FootPrintData(lastLoca.datetime, lastLoca.datetime, lastLoca);
            if(this.namer != null){
                data.name = this.namer.getName(lastLoca);
            }
            this.dataArr.add(data);
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
