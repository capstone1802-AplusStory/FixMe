package com.aplusstory.fixme;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationFileManager implements FileManager {
    public static final String FILENAME_CURRENT_LOCATION = "current_location";
    public static final String FILENAME_LOCATION_PREFIX = "location_";
    public static final String DATE_FORMAT_GMT_DATE = "yyyy-MM-dd";

    public static final int FLAG_READ = 0x01;
    public static final int FLAG_WRITE = 0x02;

    public static final int READ_ONLY = FLAG_READ;
    public static final int WRITE_ONLY = FLAG_WRITE;
    public static final int READ_WRITE = FLAG_READ | FLAG_WRITE;

    private SharedPreferences sp = null;
    private FileWriter fwToday = null;
    private FileReader frToday = null;
    private DateFormat dfDate = null;
    private Calendar cal = null;

    Context context;

    public static String getFilename(Context context, Date date){
        return context.getFilesDir()
                + "/" + FILENAME_LOCATION_PREFIX
                + new SimpleDateFormat(DATE_FORMAT_GMT_DATE, Locale.US)
                .format(date);
    }

    public static String getFilenameForToday(Context context) {
        return LocationFileManager.getFilename(context, new Date(System.currentTimeMillis()));
    }



    public String getFilename(Date date){
        return this.context.getFilesDir() + "/" + FILENAME_LOCATION_PREFIX + dfDate.format(date);
    }
    public String getFilename(long dateTimeInMillisec){
        return this.getFilename(new Date(dateTimeInMillisec));
    }
    public String getFilename(Calendar today){
        return this.getFilename(today.getTime());
    }

    public String getFilenameForToday(){
        return this.getFilename(this.cal);
    }

    public LocationFileManager(Context context){
        this(context, true, true);
    }

    public LocationFileManager(Context context, int flag){
        this(context, (flag & FLAG_READ) != 0, (flag & FLAG_WRITE) != 0);
    }

    public LocationFileManager(Context context, boolean read, boolean write){
        this.context = context;
        this.sp = context.getSharedPreferences(FILENAME_CURRENT_LOCATION, 0);
        this.dfDate = new SimpleDateFormat(DATE_FORMAT_GMT_DATE, Locale.US);
        this.cal = Calendar.getInstance();
        Date now = new Date(System.currentTimeMillis());
        this.cal.setTime(now);

        if(write) {
            try {
                this.fwToday = new FileWriter(this.getFilenameForToday(), true);
            } catch (Exception e) {
                this.fwToday = null;
                //error handle
            }
        }

        if(read) {
            try {
                this.frToday = new FileReader(this.getFilenameForToday());
            } catch (Exception e) {
                Log.d(LocationFileManager.class.getName(), e.toString());
                this.frToday = null;
            }
        }
    }

    @Nullable
    @Override
    public String getData(String fileName) {
        StringBuilder sb = new StringBuilder();
        Scanner sc = null;
//        FileReader fr;
//        if(this.frToday != null && fileName.equals(this.getFilenameForToday())){
//            fr = this.frToday;
//        } else {
//            try{
//                fr = new FileReader(fileName);
//            } catch (IOException e){
//                Log.d(LocationFileManager.class.getName(), e.toString());
//                fr = null;
//            }
//        }
        try{
            sc = new Scanner(new File(fileName));
        }catch (IOException e){
                Log.d(LocationFileManager.class.getName(), e.toString());
                sc = null;
        }

//        if(fr != null){
        if(sc != null){
//            try{
//                CharBuffer buf = CharBuffer.allocate(255);
                 sb = new StringBuilder();
//                int i;
//                while (fr.ready()){
//                    i = fr.read(buf);
//                    sb.append(buf);
//                }
                while (sc.hasNext()){
                    String s = sc.nextLine();
//                    Log.d(this.getClass().getName(), "string to read : " + s);
                    sb.append(s);
                }
//            } catch(IOException e){
//                Log.d(LocationFileManager.class.getName(), e.toString());
//            }
        }

        return sb.toString();
    }

    public static LocationDataManager.LocationData getCurrentLocation(Context context){
        LocationDataManager.LocationData rt = null;
        SharedPreferences sp = context.getSharedPreferences(FILENAME_CURRENT_LOCATION, 0);
        if(sp != null) {
            rt = new LocationDataManager.LocationData(
                    sp.getString(LocationDataManager.LocationData.KEY_DATETIME, "-1")
                    , Double.parseDouble(sp.getString(LocationDataManager.LocationData.KEY_LATITUDE, "0.0"))
                    , Double.parseDouble(sp.getString(LocationDataManager.LocationData.KEY_LONGITUDE, "0.0"))
            );
        }
            return rt;
    }

    public LocationDataManager.LocationData getCurrentLocation(){
        LocationDataManager.LocationData rt = null;
        if(this.sp != null){
            rt = new LocationDataManager.LocationData(
                     this.sp.getString(LocationDataManager.LocationData.KEY_DATETIME,"-1")
                    ,Double.parseDouble(this.sp.getString(LocationDataManager.LocationData.KEY_LATITUDE, "0.0"))
                    ,Double.parseDouble(this.sp.getString(LocationDataManager.LocationData.KEY_LONGITUDE, "0.0"))
            );
        }

        return rt;
    }

    @Nullable
    public LocationDataManager.PathData getLocationList(Date date){
        String data = this.getData(this.getFilename(date));
        LocationDataManager.PathData path = null;
        ArrayList<LocationDataManager.LocationData> bufLoca = new ArrayList<>(10);
        if(data != null) {
            Log.d(this.getClass().getName(), "data : \n" + data);
            String strPattern = "\\{(.*?)\\}";
            Pattern pattern = Pattern.compile(strPattern);
            Matcher matcher = pattern.matcher(data);
//            Scanner sc = new Scanner(data);

//            while (sc.hasNext(pattern)){
//                String s = sc.next(pattern);
            while(matcher.find()){
                String s = matcher.group();
                Log.d(this.getClass().getName(), "pattern matched : " + s);
                LocationDataManager.LocationData loca = null;
                try {
                     loca = LocationDataManager.LocationData.parseJSON(new JSONObject(s));
                } catch (Exception e){
                    Log.d(this.getClass().getName(), e.toString());
                }
                if(loca != null){
                    bufLoca.add(loca);
                }
            }

            if(bufLoca.size() > 0){
                LocationDataManager.LocationData[] arr = new LocationDataManager.LocationData[bufLoca.size()];
                bufLoca.toArray(arr);
                path = new LocationDataManager.PathData(arr);
            }

        }

        return path;
    }

    @Nullable
    public LocationDataManager.PathData getLocationListForToday(){
        return this.getLocationList(this.cal.getTime());
    }

    public boolean setCurrentLocation(LocationDataManager.LocationData loca){
        boolean rt = false;
        DateFormat df = new SimpleDateFormat(LocationFileManager.DATE_FORMAT_GMT_DATE,Locale.US);
        if(this.fwToday != null){
            this.putLocation(loca);
        }
        if(this.sp != null){
            SharedPreferences.Editor ed = this.sp.edit();
            ed.putString(LocationDataManager.LocationData.KEY_LATITUDE, Double.toString(loca.latitude));
            ed.putString(LocationDataManager.LocationData.KEY_LONGITUDE, Double.toString(loca.longitude));
            Date dt = new Date(loca.datetime);
            String dtStr = df.format(dt);
            ed.putString(LocationDataManager.LocationData.KEY_DATETIME, dtStr);
            rt = ed.commit();
        }
        return rt;
    }

    @Override
    public boolean setData(String fileName, String jsonStr) {
        boolean rt = false;
        LocationDataManager.LocationData loca = null;
        FileWriter fw;

        try {
            loca = LocationDataManager.LocationData.parseJSON(new JSONObject(jsonStr));
            if(this.fwToday != null && fileName.equals(this.getFilenameForToday())){
                fw = this.fwToday;
            } else {
                try{
                    fw = new FileWriter(fileName, true);
                } catch (IOException e){
                    fw = null;
                }
            }

            if(fw != null){
                try {
                    Log.d(LocationFileManager.class.getName(), "json to write : " + jsonStr);
                    fw.write(jsonStr);
                    fw.flush();
                    fw.close();
                    rt = true;
                } catch (IOException e){

                }
            }
        }catch(JSONException e){
            Log.d(LocationFileManager.class.getName(), e.toString());
        }

        return rt;
    }

    public boolean putLocation(LocationDataManager.LocationData locationData){
        boolean rt = false;
        Calendar cal = Calendar.getInstance();
        Date now = new Date(System.currentTimeMillis());
        cal.setTime(now);
        if(this.cal.get(Calendar.DAY_OF_MONTH) != cal.get(Calendar.DAY_OF_MONTH)
        || this.cal.get(Calendar.MONTH) != cal.get(Calendar.MONTH)
        ){
          try{
              if(this.fwToday != null) {
                  this.fwToday.close();
              }
              if(this.frToday != null) {
                  this.frToday.close();
              }
          } catch (IOException e){
              Log.d(LocationFileManager.class.getName(), e.toString());
          }
          this.cal.clear();
          this.cal.setTime(now);
          try{
                this.fwToday = new FileWriter(this.getFilenameForToday(), true);
                this.frToday = new FileReader(this.getFilenameForToday());
          } catch (IOException e){
              Log.d(LocationFileManager.class.getName(), e.toString());
          }
        }

        if(locationData != null && this.fwToday != null){
            String jsonStr = locationData.toString();
            Log.d(LocationFileManager.class.getName(), "json to write : " + jsonStr);
            try {
                this.fwToday.write(jsonStr + "\n");
                rt = true;
                this.fwToday.flush();
            }  catch(Exception e){
                Log.d(LocationFileManager.class.getName(), e.toString());
            }
        }
        return rt;
    }

    public void destroy(){
        try {
            if (this.fwToday != null) {
                this.fwToday.close();
            }
            if (this.frToday != null) {
                this.frToday.close();
            }
        } catch (IOException e){
            Log.d(LocationFileManager.class.getName(), e.toString());
        }
    }
}
