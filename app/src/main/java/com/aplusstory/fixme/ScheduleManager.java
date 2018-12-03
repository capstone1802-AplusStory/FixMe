package com.aplusstory.fixme;


import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleManager implements ScheduleDataManager {
    private Context context;
    private ScheduleFileManager fm = null;
    private Map<String, ScheduleData> schBuf = null;
    private Map<Integer, String> schMonthMap = null;
    private List<String> schList = null;
    private SharedPreferences sp = null;

    public ScheduleManager(Context context){
        this.context = context;
        this.fm = new ScheduleFileManager(this.context);
        this.schBuf = new HashMap<>();
        this.schList = this.fm.getScheduleList();
        for(String s : schList){
            schBuf.put(s, this.fm.getSchedule(s));
        }
    }

    public List<String> getScheduleList(){
        ArrayList<String> rt = new ArrayList<>(this.schList.size());
        Collections.copy(rt, this.schList);
        return rt;
    }

    public List<String> getMonthlyList(int year, int month) {
        ArrayList<String> rt = new ArrayList<>();
        Calendar cBegin = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();

        for(String s : this.schList){
            ScheduleData sch = this.schBuf.get(s);
            if(sch.isRepeated){
                Calendar cRptFin = Calendar.getInstance();
                cRptFin.setTime(new Date(sch.repeatEnd));
                if(cRptFin.get(Calendar.YEAR) <= year && cRptFin.get(Calendar.MONTH) <= month) {
                    switch (sch.repeatType) {
                        case RepeatDuration.REPEAT_DAYLY:
                            rt.add(s);
                            break;
                        case RepeatDuration.REPEAT_WEEKLY:
                            rt.add(s);
                            break;
                        case RepeatDuration.REPEAT_MONTHLY:
                            rt.add(s);
                            break;
                        case RepeatDuration.REPEAT_YEARLY:
                            cBegin.setTime(new Date(sch.scheduleBegin));
                            cEnd.setTime(new Date(sch.scheduleEnd));
                            if(cBegin.get(Calendar.MONTH) == month
                            || cEnd.get(Calendar.MONTH) == month) {
                                rt.add(s);
                            }
                            break;
                        default:
                            //something wrong
                    }
                }
            } else{
                cBegin.setTime(new Date(sch.scheduleBegin));
                cEnd.setTime(new Date(sch.scheduleEnd));
                if((cBegin.get(Calendar.YEAR)   == year
                ||  cEnd.get(Calendar.YEAR)     == year)
                && (cBegin.get(Calendar.MONTH)  == month
                ||  cEnd.get(Calendar.MONTH)    == month)){
                    rt.add(s);
                }
            }
        }

        return rt;
    }

    @Override
    @Nullable
    public ScheduleData getData(String name){
        if(this.schBuf.containsKey(name))
            return this.schBuf.get(name);
        else
            return null;
    }

    @Override
    public boolean putData(ScheduleData sch) {
        if(sch.hasAlarm){
            AlarmManager alm = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
            SharedPreferences sp = context.getSharedPreferences(ScheduleAlarmManager.FILENAME_SCHEDULE_ALARM_CODE, 0);
            if(alm != null && sp != null) {
                int reqCode;
                if(sp.contains(sch.name)){
                    reqCode = sp.getInt(sch.name, -1);
                } else{
                    reqCode = (int)System.currentTimeMillis();
                }
                ScheduleAlarmManager.setAlarm(this.context, alm, sch, reqCode);
            }
        }

        if(this.fm != null && !this.schList.contains(sch.name)){
            return this.fm.putSchedule(sch);
        } else {
            return false;
        }
    }

    @Override
    public void setFileManager(FileManager f) {
        if(f instanceof ScheduleFileManager){
            this.fm = (ScheduleFileManager) f;
        }
    }
}
