package com.aplusstory.fixme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class ScheduleAlarmManager extends Service {
    public static final String SCHEDULE_ALARM_START_ACTION = "com.aplusstory.fixme.action.ALARM_START";
    public static final String SCHEDULE_ALARM_CANCEL_ACTION = "com.aplusstory.fixme.action.ALARM_CANCEL";

    public static final String FILENAME_SCHEDULE_ALARM_CODE = "schedule_alarm";

    public static final String KEY_LOCATON = "location";
    public static final String KEY_SCHEDULE = "scheduleData";
    public static final double RANGE_ALARM = 25.0;
    public static final Class ALARM_ACTIVITY = null;//set the alarm activity here

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ScheduleDataManager.ScheduleData sch;
        LocationDataManager.LocatonData loca;
        sch = (ScheduleDataManager.ScheduleData)intent.getBundleExtra(KEY_SCHEDULE).getSerializable(KEY_SCHEDULE);

        if(sch != null){
            loca = new LocationDataManager.LocatonData(sch.scheduleBegin, sch.latitude, sch.longitude);
        } else {
            loca = (LocationDataManager.LocatonData)intent.getBundleExtra(KEY_LOCATON).getSerializable(KEY_LOCATON);
        }

        SharedPreferences spCrnLoca = this.getSharedPreferences(LocationFileManager.FILENAME_CURRENT_LOCATION,0);
        long now = System.currentTimeMillis();
        double lat = Double.parseDouble(spCrnLoca.getString(LocationDataManager.LocatonData.KEY_LATITUDE, "0.0"));
        double longt = Double.parseDouble(spCrnLoca.getString(LocationDataManager.LocatonData.KEY_LONGITUDE, "0.0"));
        LocationDataManager.LocatonData currentLoca = new LocationDataManager.LocatonData(now, lat, longt);

        if(sch != null && sch.isRepeated && now < sch.repeatEnd){
            SharedPreferences spAlm = this.getSharedPreferences(ScheduleAlarmManager.FILENAME_SCHEDULE_ALARM_CODE, 0);
            AlarmManager alm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(sch.scheduleBegin));
            if(spAlm != null && alm != null){
                switch (sch.repeatType){
                    case ScheduleDataManager.RepeatDuration.REPEAT_DAYLY:
                        c.add(Calendar.DATE, +1);
                        break;
                    case ScheduleDataManager.RepeatDuration.REPEAT_WEEKLY:
                        c.add(Calendar.WEEK_OF_MONTH, +1);
                        break;
                    case ScheduleDataManager.RepeatDuration.REPEAT_MONTHLY:
                        c.add(Calendar.MONTH, +1);
                        break;
                    case ScheduleDataManager.RepeatDuration.REPEAT_YEARLY:
                        c.add(Calendar.YEAR, +1);
                        break;
                    default:
                        //something wrong
                }
                int reqCode = spAlm.getInt(sch.name, -1);
                if(reqCode > 0){
                    Intent it = new Intent(intent);
                    PendingIntent pit = PendingIntent.getService(this, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pit);
                }
            }
        }

        if(loca != null && currentLoca.distanceTo(loca) > RANGE_ALARM) {
            Intent it = new Intent(this, ALARM_ACTIVITY);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(it);
        }

        return START_NOT_STICKY;
    }

    public static int setAlarm(Context context,
                               AlarmManager alm,
                               long time,
                               LocationDataManager.LocatonData loca,
                               int requestCode){
        int rt = -1;
        if(alm != null){
            Intent it = new Intent(context, ScheduleAlarmManager.class);
            it.setAction(ScheduleAlarmManager.SCHEDULE_ALARM_START_ACTION);
            Bundle bd = new Bundle();
            bd.putSerializable(KEY_LOCATON, loca);
            it.putExtra(KEY_LOCATON, bd);
            PendingIntent pit;
            pit = PendingIntent.getService(context,requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT);
            alm.set(AlarmManager.RTC_WAKEUP, time, pit);
            rt = requestCode;
        }
        return rt;
    }

    public static int setAlarm(Context context,
                               AlarmManager alm,
                               ScheduleDataManager.ScheduleData sch,
                               int requestCode){
        int rt = -1;
        if(alm != null && sch.isValid()){
            Intent it = new Intent(context, ScheduleAlarmManager.class);
            it.setAction(ScheduleAlarmManager.SCHEDULE_ALARM_START_ACTION);
            Bundle bd = new Bundle();
            bd.putSerializable(KEY_SCHEDULE, sch);
            it.putExtra(KEY_SCHEDULE, sch);
            PendingIntent pit;
            pit = PendingIntent.getService(context, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT);
            alm.set(AlarmManager.RTC_WAKEUP
            , sch.scheduleBegin - ScheduleDataManager.AlarmInterval.getTime(sch.alarmInterval)
            , pit);
            SharedPreferences sp;
            sp = context.getSharedPreferences(ScheduleAlarmManager.FILENAME_SCHEDULE_ALARM_CODE, 0);
            sp.edit().putInt(sch.name, requestCode).apply();
            rt = requestCode;
        }
        return rt;
    }

    public static int cancelAlarm(Context context, AlarmManager alm, String schName, int requestCode){
        int rt = -1;
        if(alm != null){
            Intent it = new Intent(context, ScheduleAlarmManager.class);
            it.setAction(ScheduleAlarmManager.SCHEDULE_ALARM_START_ACTION);
            PendingIntent pit;
            pit = PendingIntent.getService(context, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT);
            alm.cancel(pit);
            SharedPreferences sp;
            sp = context.getSharedPreferences(ScheduleAlarmManager.FILENAME_SCHEDULE_ALARM_CODE, 0);
            sp.edit().putInt(schName, requestCode).apply();
            rt = requestCode;
        }

        return rt;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
