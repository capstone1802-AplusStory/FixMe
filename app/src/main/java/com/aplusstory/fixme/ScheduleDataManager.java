package com.aplusstory.fixme;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.CharArrayReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public interface ScheduleDataManager extends UserDataManager{

    @Override
    void setFileManager(FileManager f);
    ScheduleData getData(String name);
    boolean putData(ScheduleData sch);

    class ScheduleData implements Serializable {
        public static final String KEY_NAME = "name";
        public static final String KEY_REPEATED = "is_repeated";
        public static final String KEY_REPEAT_TYPE_CODE = "repeat_type";
        public static final String KEY_REPEAT_DAY_OF_WEEK = "repeat_day_of_week";
        public static final String KEY_REPEAT_END = "repeat_end";
        public static final String KEY_DATE_SCHEDULE_BEGIN = "schedule_begin";
        public static final String KEY_DATE_SCHEDULE_END = "schedule_end";
        public static final String KEY_LOCATION = "location";
        public static final String KEY_LOCATION_TAG = "location_address";
        public static final String KEY_MEMO = "memo";
        public static final String KEY_ALARM_INTERVAL_CODE = "alarm_interval";
        public static final String KEY_TABLE_COLOR_CODE = "table_color";

        public static final String DATE_FORMAT_GMT = "yyyy-MM-ddHH:mm:ss";

        String name = null;
        boolean isRepeated = false;
        int repeatType = -1;
        boolean[] repeatDayOfWeek = {false, false, false, false, false, false, false, false};
        //repeatDayOfWeek[1] : sunday, ... , repeatDayOfWeek[7] : saturday, repeatDayOfWeek[0]
        //repeatDayOfWeek[0] : repeatType == RepeatDuration.REPEAT_DAYLY
        long repeatEnd = -1;
        long scheduleBegin = -1;
        long scheduleEnd = -1;
        boolean hasLocation = false;
        Double latitude = 0.0;
        Double longitude = 0.0;
        String locationAddress = null;
        String memo = null;
        boolean hasAlarm = false;
        int alarmInterval = -1;
        int tableColor = -1;

        public ScheduleData(){

        }

        public ScheduleData(ScheduleData target){
            this.name = new String(target.name);
            this.isRepeated = target.isRepeated;
            if(this.isRepeated){
                this.repeatType = target.repeatType;
                if(this.repeatType == RepeatDuration.REPEAT_WEEKLY){
                    System.arraycopy(this.repeatDayOfWeek, 0, target.repeatDayOfWeek, 0, this.repeatDayOfWeek.length);
                }
                this.repeatEnd = target.repeatEnd;
            }
            this.scheduleBegin = target.scheduleBegin;
            this.scheduleEnd = target.scheduleEnd;
            this.hasLocation = target.hasLocation;
            if(this.hasLocation){
                this.latitude = target.latitude;
                this.longitude = target.longitude;
                this.locationAddress = new String(target.locationAddress);
            }
            this.memo = new String(target.memo);
            this.hasAlarm = target.hasAlarm;
            if(this.hasAlarm){
                this.alarmInterval = target.alarmInterval;
            }
            this.tableColor = target.tableColor;
        }

        @Nullable
        public static ScheduleData parseJSON(JSONObject json){
            ScheduleData sch = new ScheduleData();
            DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT);

            try{
                sch.name = json.getString(KEY_NAME);
                sch.isRepeated = json.getBoolean(KEY_REPEATED);
                if(sch.isRepeated) {
                    sch.repeatType = json.getInt(KEY_REPEAT_TYPE_CODE);
                    if (sch.isRepeated && sch.repeatType == RepeatDuration.REPEAT_WEEKLY) {
                        sch.repeatDayOfWeek[0] = true;
                        JSONArray arr = json.getJSONArray(KEY_REPEAT_DAY_OF_WEEK);
                        for (int i = 0; i < arr.length(); i++) {
                            sch.repeatDayOfWeek[arr.getInt(i)] = true;
                        }
                    }
                    try {
                        sch.repeatEnd = df.parse(json.getString(KEY_REPEAT_END)).getTime();
                    } catch (ParseException e) {
                        Log.d(ScheduleData.class.getName(), e.toString());
                    }
                }
                sch.scheduleBegin = df.parse(json.getString(KEY_DATE_SCHEDULE_BEGIN)).getTime();
                sch.scheduleEnd = df.parse(json.getString(KEY_DATE_SCHEDULE_END)).getTime();
                if(json.has(KEY_LOCATION)){
                    sch.hasLocation = true;
                    LocationDataManager.LocationData loca =
                    LocationDataManager.LocationData.parseJSON(json.getJSONObject(KEY_LOCATION), new SimpleDateFormat(DATE_FORMAT_GMT));
                    if(loca != null) {
                        sch.latitude = loca.latitude;
                        sch.longitude = loca.longitude;
                        if(json.has(KEY_LOCATION_TAG)) {
                            sch.locationAddress = json.getString(KEY_LOCATION_TAG);
                        }
                    }
                }

                sch.memo = json.getString(KEY_MEMO);
                if(json.has(KEY_ALARM_INTERVAL_CODE)) {
                    sch.hasAlarm = true;
                    sch.alarmInterval = json.getInt(KEY_ALARM_INTERVAL_CODE);
                }
                sch.tableColor = json.getInt(KEY_TABLE_COLOR_CODE);
            } catch(Exception e){
                Log.d(ScheduleData.class.getName(), e.toString());
                sch = null;
            }


            return sch;
        }

        @Nullable
        public JSONObject JSONify(){
            JSONObject json = new JSONObject();
            DateFormat df = new SimpleDateFormat(DATE_FORMAT_GMT);

            try {
                json.put(KEY_NAME, this.name);
                json.put(KEY_REPEATED, this.isRepeated);
                if(this.isRepeated) {
                    json.put(KEY_REPEAT_TYPE_CODE, this.repeatType);

                    if (this.repeatDayOfWeek[0]) {
                        JSONArray arr = new JSONArray();
                        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
                            if (repeatDayOfWeek[i]) {
                                arr.put(i);
                            }
                        }
                        if (arr.length() > 0) {
                            json.put(KEY_REPEAT_DAY_OF_WEEK, arr);
                        }
                    }
                    json.put(KEY_REPEAT_END, df.format(new Date(this.repeatEnd)));
                }
                json.put(KEY_DATE_SCHEDULE_BEGIN, df.format(this.scheduleBegin));
                json.put(KEY_DATE_SCHEDULE_END, df.format(this.scheduleEnd));
                if(this.hasLocation){
                    LocationDataManager.LocationData loca;
                    loca = new LocationDataManager.LocationData(
                            this.scheduleBegin, this.latitude, this.longitude);
                    json.put(KEY_LOCATION, loca.JSONify());
                    json.put(KEY_LOCATION_TAG, this.locationAddress);
                }

                json.put(KEY_MEMO, this.memo);
                if(this.hasAlarm) {
                    json.put(KEY_ALARM_INTERVAL_CODE, this.alarmInterval);
                }
                json.put(KEY_TABLE_COLOR_CODE, this.tableColor);
            }catch(JSONException e){
                Log.d(this.getClass().getName(), e.toString());
                json = null;
            }

            return json;
        }

        @Override
        public String toString() {
            return this.JSONify().toString();
        }

        public boolean isValid(){
            boolean rt = false;

            if(this.scheduleBegin > this.scheduleEnd){
                rt = false;
            } else if(this.name == null || this.name.length() == 0){
                rt = false;
            } else{
                rt = true;
            }

            return rt;
        }

        public boolean isValidNow(){
            return this.isValidNow(System.currentTimeMillis());
        }

        public boolean isValidNow(long now){
            boolean rt = this.isValid();

            if(rt && this.isRepeated){
                if(now > this.scheduleEnd){
                    rt = false;
                }
                if(now > this.repeatEnd){
                    rt = false;
                } else {
                    Date today = new Date(now);
                    Date begin = new Date(this.scheduleBegin);
                    Date end = new Date(this.scheduleEnd);
                    Calendar cToday = Calendar.getInstance();
                    Calendar cBegin = Calendar.getInstance();
                    Calendar cEnd = Calendar.getInstance();
                    cToday.setTime(today);
                    cBegin.setTime(begin);
                    cEnd.setTime(end);
                    int[] n, b, e;
                    switch (this.repeatType){
                        case RepeatDuration.REPEAT_DAYLY:
                            n = new int[]{cToday.get(Calendar.HOUR), cToday.get(Calendar.MINUTE)};
                            b = new int[]{cBegin.get(Calendar.HOUR), cBegin.get(Calendar.MINUTE)};
                            e = new int[]{cEnd.get(Calendar.HOUR), cEnd.get(Calendar.MINUTE)};
                            rt = (n[0] >= b[0] && n[0] <= e[0]) && (n[1] >= b[1] && n[1] <= b[1]);
                            break;
                        case RepeatDuration.REPEAT_WEEKLY:
                            n = new int[]{cToday.get(Calendar.HOUR), cToday.get(Calendar.MINUTE)};
                            b = new int[]{cBegin.get(Calendar.HOUR), cBegin.get(Calendar.MINUTE)};
                            e = new int[]{cEnd.get(Calendar.HOUR), cEnd.get(Calendar.MINUTE)};
                            rt = this.repeatDayOfWeek[cToday.get(Calendar.DAY_OF_WEEK)]
                                && (n[0] >= b[0] && n[0] <= e[0])
                                && (n[1] >= b[1] && n[1] <= b[1]);
                            break;
                        case RepeatDuration.REPEAT_MONTHLY:
                            n = new int[]{cToday.get(Calendar.DAY_OF_MONTH)
                                        , cToday.get(Calendar.HOUR)
                                        , cToday.get(Calendar.MINUTE)};
                            b = new int[]{cBegin.get(Calendar.DAY_OF_MONTH)
                                        , cBegin.get(Calendar.HOUR)
                                        , cBegin.get(Calendar.MINUTE)};
                            e = new int[]{cBegin.get(Calendar.DAY_OF_MONTH)
                                        , cEnd.get(Calendar.HOUR)
                                        , cEnd.get(Calendar.MINUTE)};
                            rt =    (n[0] >= b[0] && n[0] <= e[0])
                                &&  (n[1] >= b[1] && n[1] <= b[1])
                                &&  (n[2] >= b[2] && n[2] <= b[2]);
                            break;
                        case RepeatDuration.REPEAT_YEARLY:
                            n = new int[]{cToday.get(Calendar.MONTH)
                                    , cToday.get(Calendar.DAY_OF_MONTH)
                                    , cToday.get(Calendar.HOUR)
                                    , cToday.get(Calendar.MINUTE)};
                            b = new int[]{cBegin.get(Calendar.MONTH)
                                    , cBegin.get(Calendar.DAY_OF_MONTH)
                                    , cBegin.get(Calendar.HOUR)
                                    , cBegin.get(Calendar.MINUTE)};
                            e = new int[]{cEnd.get(Calendar.MONTH)
                                    , cBegin.get(Calendar.DAY_OF_MONTH)
                                    , cEnd.get(Calendar.HOUR)
                                    , cEnd.get(Calendar.MINUTE)};
                            rt = (n[0] >= b[0] && n[0] <= e[0])
                                    &&  (n[1] >= b[1] && n[1] <= b[1])
                                    &&  (n[2] >= b[2] && n[2] <= b[2])
                                    &&  (n[3] >= b[3] && n[3] <= b[3]);
                            break;
                        default:
                            //something wrong
                    }
                }
            } else if(rt) {
                rt = this.scheduleBegin <= now && this.scheduleEnd >= now;
            }


            return rt;
        }
    }

    public static class AlarmInterval {
        public static final int INTERVAL_AMINUTE = 0;
        public static final int INTERVAL_FIVEMINUTE = 1;
        public static final int INTERVAL_TENMINUTE = 2;
        public static final int INTERVAL_THIRTYMINUTE = 3;
        public static final int INTERVAL_ANHOUR = 4;
        public static final int INTERVAL_SIXHOUR = 5;

        public static final long TIME_AMINUTE = 60000;
        public static final long TIME_FIVEMINUTE = TIME_AMINUTE * 5;
        public static final long TIME_TENMINUTE = 600000;
        public static final long TIME_THIRTYMINUTE = TIME_TENMINUTE * 3;
        public static final long TIME_ANHOUR = TIME_AMINUTE * 60;
        public static final long TIME_SIXHOUR = 6 * TIME_ANHOUR;

        public static long getTime(int alarmTimeCode){
            switch(alarmTimeCode){
                case INTERVAL_AMINUTE:
                    return TIME_AMINUTE;
                case INTERVAL_FIVEMINUTE:
                    return TIME_AMINUTE;
                case INTERVAL_TENMINUTE:
                    return TIME_FIVEMINUTE;
                case INTERVAL_THIRTYMINUTE:
                    return TIME_THIRTYMINUTE;
                case INTERVAL_ANHOUR:
                    return TIME_ANHOUR;
                case INTERVAL_SIXHOUR:
                    return TIME_SIXHOUR;
                default:
                    return -1;
            }
        }

        public static String getTimeText(int alarmTimeCode){
            switch (alarmTimeCode){
                case INTERVAL_AMINUTE:
                    return "1분 전";
                case INTERVAL_FIVEMINUTE:
                    return "5분 전";
                case INTERVAL_TENMINUTE:
                    return "10분 전";
                case INTERVAL_THIRTYMINUTE:
                    return "30분 전";
                case INTERVAL_ANHOUR:
                    return "1시간 전";
                case INTERVAL_SIXHOUR:
                    return "6시간 전";
                default:
                    return "";
            }
        }

        public static String getTimeText(Context context, int alarmTimeCode){
            switch (alarmTimeCode){
                case INTERVAL_AMINUTE:
                    return "1분 전";
                case INTERVAL_FIVEMINUTE:
                    return "5분 전";
                case INTERVAL_TENMINUTE:
                    return "10분 전";
                case INTERVAL_THIRTYMINUTE:
                    return "30분 전";
                case INTERVAL_ANHOUR:
                    return "1시간 전";
                case INTERVAL_SIXHOUR:
                    return "6시간 전";
                default:
                    return "";
            }
        }
    }

    public static class RepeatDuration {
        public static final int REPEAT_DAYLY = 0;
        public static final int REPEAT_WEEKLY = 1;
        public static final int REPEAT_MONTHLY = 2;
        public static final int REPEAT_YEARLY = 3;

        public static String getRepeatitionText(int repeatCode, boolean[] weeklyArr){
            StringBuilder sb = new StringBuilder();
            switch(repeatCode){
                case REPEAT_DAYLY:
                    sb.append("Every Day");
                    break;
                case REPEAT_WEEKLY:
                    String tmpStr = null;
                    boolean cond = false;
                    if(weeklyArr != null && weeklyArr.length == 8) {
                        for(int i = 0; i < 8; i++){
                            switch (i){
                                case 0:
                                    tmpStr = "Every";
                                    break;
                                case 1:
                                    tmpStr ="Sun";
                                    break;
                                case 2:
                                    tmpStr = "Mon";
                                    break;
                                case 3:
                                    tmpStr = "Tue";
                                    break;
                                case 4:
                                    tmpStr = "Wed";
                                    break;
                                case 5:
                                    tmpStr = "Thu";
                                    break;
                                case 6:
                                    tmpStr = "Fri";
                                    break;
                                case 7:
                                    tmpStr = "Sat";
                                    break;
                            }
                            if(weeklyArr[i]){
                                sb.append(tmpStr);
                            } else if(i == 0){
                                break;
                            } else{
                                cond = true;
                            }
                        }
                    }

                    if(!cond){

                    }
                    break;
                case REPEAT_MONTHLY:
                    sb.append("Every Month");
                    break;
                case REPEAT_YEARLY:
                    sb.append("Every Year");
                    break;
                default:
                    sb.append("None");
            }


            return sb.toString();
        }

        public static String getRepeatitionText(Context context, int repeatCode, boolean[] weeklyArr){
            StringBuilder sb = new StringBuilder();
            switch(repeatCode){
                case REPEAT_DAYLY:
                    sb.append("");
                    break;
                case REPEAT_WEEKLY:
                    sb.append("");
                    break;
                case REPEAT_MONTHLY:
                    sb.append("");
                    break;
                case REPEAT_YEARLY:
                    sb.append("");
                    break;
                default:
                    sb.append("");
            }


            return sb.toString();
        }
    }

    public static class TableColor {
        public static final int WHITE = 0;
        public static final int RED = 1;
        public static final int PINK = 2;
        public static final int YELLOW = 3;
        public static final int YELLOWGREEN = 4;
        public static final int GREEN = 5;
        public static final int MINT = 6;
        public static final int SKYBLUE = 7;
        public static final int BLUE = 8;
        public static final int PURPLE = 9;
        public static final int BLACK = 10;

        public static String getColorText(int colorCode){
            switch (colorCode){
                case BLACK:
                    return "black";
                case RED:
                    return "red";
                case BLUE:
                    return "blue";
                case GREEN:
                    return "green";
                case YELLOW:
                    return "yellow";
                case PURPLE:
                    return "purple";
                case PINK:
                    return "pink";
                case MINT:
                    return "mint";
                case SKYBLUE:
                    return "skyblue";
                case YELLOWGREEN:
                    return "yellowgreen";
                case WHITE:
                    return "white";
                default:
                    return "";
            }
        }

        public static String getColorText(Context context, int colorCode){
            switch (colorCode){
                case BLACK:
                    return "black";
                case RED:
                    return "red";
                case BLUE:
                    return "blue";
                case GREEN:
                    return "green";
                case YELLOW:
                    return "yellow";
                case PURPLE:
                    return "purple";
                case PINK:
                    return "pink";
                case MINT:
                    return "mint";
                case SKYBLUE:
                    return "skyblue";
                case YELLOWGREEN:
                    return "lightGreen";
                case WHITE:
                    return "white";
                default:
                    return "";
            }
        }
    }
}
