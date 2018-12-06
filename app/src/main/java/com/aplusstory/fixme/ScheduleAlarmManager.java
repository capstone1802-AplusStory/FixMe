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
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class ScheduleAlarmManager extends Service {
    public static final String SCHEDULE_ALARM_START_ACTION = "com.aplusstory.fixme.action.ALARM_START";
    public static final String SCHEDULE_ALARM_CANCEL_ACTION = "com.aplusstory.fixme.action.ALARM_CANCEL";

    public static final String FILENAME_SCHEDULE_ALARM_CODE = "schedule_alarm";

    public static final String KEY_LOCATON = "location";
    public static final String KEY_SCHEDULE = "scheduleData";
//    public static final String KEY_SCHEDULE_START = "begin";
//    public static final String KEY_SCHEDULE_END = "end";
//    public static final String KEY_REPEATION = "repeation_code";
//    public static final String KEY_WEEK_REPEATON = "weekly_repeation";
//    public static final String KEY_REPEATION_END = "repeation_end";

    public static final double RANGE_ALARM = 25.0;
    public static final Class ALARM_ACTIVITY = FullAlarmActivity.class;//set the alarm activity here


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ScheduleDataManager.ScheduleData sch = null;
        LocationDataManager.LocationData loca = null;
        if(intent.hasExtra(KEY_SCHEDULE)) {
            sch = (ScheduleDataManager.ScheduleData) intent.getBundleExtra(KEY_SCHEDULE).getSerializable(KEY_SCHEDULE);
        } else {
            Log.d(this.getClass().getName(), "no schedule");
        }

        if(sch != null && sch.hasLocation){
            loca = new LocationDataManager.LocationData(sch.scheduleBegin, sch.latitude, sch.longitude);
        } else if(intent.hasExtra(KEY_LOCATON)){
            loca = (LocationDataManager.LocationData)intent.getBundleExtra(KEY_LOCATON).getSerializable(KEY_LOCATON);
        } else if(sch != null && !sch.hasLocation){
            loca = null;
        }

        SharedPreferences spCrnLoca = this.getSharedPreferences(LocationFileManager.FILENAME_CURRENT_LOCATION,0);
        long now = System.currentTimeMillis();
        double lat = Double.parseDouble(spCrnLoca.getString(LocationDataManager.LocationData.KEY_LATITUDE, "0.0"));
        double longt = Double.parseDouble(spCrnLoca.getString(LocationDataManager.LocationData.KEY_LONGITUDE, "0.0"));
        LocationDataManager.LocationData currentLoca = new LocationDataManager.LocationData(now, lat, longt);

        if(sch != null && sch.isRepeated && now < sch.repeatEnd){
            Log.d(this.getClass().getName(), "on alarm, schedule : \n" + sch.toString());
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
                        int delta = 7;
                        int today = c.get(Calendar.DAY_OF_WEEK);
                        int i = today;

                        do{
                            if(sch.repeatDayOfWeek[i + 1]){
                                delta = (i - today) % 7;
                                break;
                            }
                            i++;
                            if(i > 7){
                                i = 0;
                            }
                        }while(i == today);

                        c.add(Calendar.DAY_OF_MONTH, +delta);
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
            Log.d(this.getClass().getName(), "alarm fired, \ncurrent location : "
                    + currentLoca.toString()
                    + "\n schedule location : "
                    + loca.toString());
            Intent it = new Intent(this, ALARM_ACTIVITY);
            if(sch != null){
                Bundle bd = new Bundle();
                bd.putSerializable(KEY_SCHEDULE, sch);
                it.putExtra(KEY_SCHEDULE, bd);
            }
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(it);
        } else {
            Log.d(this.getClass().getName(), "no location");
        }
        if(sch != null){
            Log.d(this.getClass().getName(), "alarm schedule : " + sch.toString());
        }

        return START_NOT_STICKY;
    }

    public static int setAlarm(Context context,
                               AlarmManager alm,
                               long time,
                               LocationDataManager.LocationData loca,
                               int requestCode){
        int rt = -1;
        if(alm != null){
            Log.d(context.getClass().getName(), "set alarm, location : \n" + loca.toString());
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
        boolean cond = true;
        if(alm != null && (cond = sch.isValid())){
            Log.d(context.getClass().getName(), "set alarm, shcedule : \n" + sch.toString());
            Intent it = new Intent(context, ScheduleAlarmManager.class);
            it.setAction(ScheduleAlarmManager.SCHEDULE_ALARM_START_ACTION);
            Bundle bd = new Bundle();
            bd.putSerializable(KEY_SCHEDULE, sch);
            it.putExtra(KEY_SCHEDULE, bd);
            if(!it.hasExtra(KEY_SCHEDULE)){
                Log.d(context.getClass().getName(), "wtf");
            }
            PendingIntent pit;
            pit = PendingIntent.getService(context, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT);
            alm.set(AlarmManager.RTC_WAKEUP
            , sch.scheduleBegin - ScheduleDataManager.AlarmInterval.getTime(sch.alarmInterval)
            , pit);
            SharedPreferences sp;
            sp = context.getSharedPreferences(ScheduleAlarmManager.FILENAME_SCHEDULE_ALARM_CODE, 0);
            sp.edit().putInt(sch.name, requestCode).apply();
            rt = requestCode;
        } else if(!cond){
            Log.d(context.getClass().getName(), "schedule is not valid");
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
