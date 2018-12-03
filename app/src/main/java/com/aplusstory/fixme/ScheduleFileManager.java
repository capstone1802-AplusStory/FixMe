package com.aplusstory.fixme;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ScheduleFileManager implements FileManager {
    public static final String FILENAME_SCHEDULE_PREFIX = "schedule_";
    public static final String FILENAME_SCHEDULE_LIST = FILENAME_SCHEDULE_PREFIX + "list";

    private Context context;
    private SharedPreferences sp = null;
    private Map<String, String> listSch;

    public ScheduleFileManager(Context context){
        this.context = context;
        this.sp = context.getSharedPreferences(FILENAME_SCHEDULE_LIST, 0);
        Map<String, ?> listSch = this.sp.getAll();
        this.listSch = new HashMap<>();
        for(String s : listSch.keySet()){
            this.listSch.put(s, listSch.get(s).toString());
        }
    }

    private void refreshData(){
        Map<String, ?> listSch = this.sp.getAll();
        this.listSch = new HashMap<>();
        for(String s : listSch.keySet()){
            this.listSch.put(s, listSch.get(s).toString());
        }
    }

    public List<String> getScheduleList(){
        return new ArrayList<>(this.listSch.keySet());
    }

    @Nullable
    public ScheduleDataManager.ScheduleData getSchedule(String name){
        ScheduleDataManager.ScheduleData sch = null;
        if(listSch.containsKey(name)){
            try {
               sch = ScheduleDataManager.ScheduleData.parseJSON(new JSONObject(this.getData(name)));
            }catch(Exception e){
                Log.d(this.getClass().getName(), e.toString());
            }
        }

        return sch;
    }
    public boolean putSchedule(ScheduleDataManager.ScheduleData sch){
        boolean rt = false;
        FileWriter fw = null;
        String filepath;

        if(this.listSch.containsKey(sch.name)){
            return rt;
//            filepath = this.listSch.get(sch.name);
        } else{
            filepath = context.getFilesDir() + "/" + FILENAME_SCHEDULE_PREFIX + sch.name;
        }

        try{
            fw = new FileWriter(filepath);
//            fw = new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_16);
            String toWrite = sch.toString();
            Log.d(this.getClass().getName(), "data to write : \n" + toWrite);
            fw.write(toWrite);
            rt = true;
            fw.flush();
            this.sp.edit().putString(sch.name, filepath).apply();
        } catch(IOException e){
            Log.d(this.getClass().getName(), e.toString());
        }

        if(rt){
            this.refreshData();
        }

        return rt;
    }

    @Nullable
    @Override
    public String getData(String schName) {
        Reader fr = null;
        StringBuilder sb = new StringBuilder();
        Scanner sc;
        try {
            File f = new File(this.listSch.get(schName));
            Log.d(this.getClass().getName(), "file to read : " + f.getAbsolutePath());
            sc = new Scanner(f);
//            fr = new InputStreamReader(new FileInputStream(this.listSch.get(schName)), StandardCharsets.UTF_16);
//            CharBuffer buf = CharBuffer.allocate(255);
            int i;
            while (sc.hasNext()) {
//                i = fr.read(buf);
                sb.append(sc.next());
            }
        }catch (Exception e){
            Log.d(this.getClass().getName(), e.toString());
        }
        String rt = sb.toString();
        Log.d(this.getClass().getName(), "data to read : \n" + rt);
        return rt;
    }

    @Override
    public boolean setData(String schName, String jsonSch) {
        ScheduleDataManager.ScheduleData sch;
        try {
            if ((sch = ScheduleDataManager.ScheduleData.parseJSON(new JSONObject(jsonSch))) != null)
                return this.putSchedule(sch);
            else
                return false;
        } catch(JSONException e){
            return false;
        }
    }
}
