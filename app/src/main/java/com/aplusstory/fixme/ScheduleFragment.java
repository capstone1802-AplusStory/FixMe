package com.aplusstory.fixme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_KEY_SCHEDULE = "argument_schedule";
    public static final String ARG_KEY_NAME = "argument_schedule_name";
    public static final String ARG_KEY_EDIT = "argument_edit";
    public static final String ARG_KEY_DELETE = "argument_delete";
    public static final String ARG_KEY_TODAY = "argument_today";

    private Bundle arg = null;
    Context context;

    Date today;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private int REQUEST_RESULT = 1;
    private ScheduleDataManager.ScheduleData sch = new ScheduleDataManager.ScheduleData();

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ScheduleFragment.class.toString(),ScheduleFragment.class.toString() + " created");
        if(this.getArguments() != null){
            this.arg = new Bundle(this.getArguments());
            if(this.arg.containsKey(ARG_KEY_SCHEDULE)){
                this.sch = (ScheduleDataManager.ScheduleData) this.arg.getSerializable(ARG_KEY_SCHEDULE);
                this.today = new Date(sch.scheduleBegin);
            } else if (this.arg.containsKey(ARG_KEY_EDIT)){
                this.sch = (ScheduleDataManager.ScheduleData) this.arg.getSerializable(ARG_KEY_EDIT);
                this.today = new Date(sch.scheduleBegin);
            } else {
                if(this.arg.containsKey(ARG_KEY_TODAY)){
                    this.today = new Date(this.arg.getLong(ARG_KEY_TODAY));
                } else {
                    this.today = new Date(System.currentTimeMillis());
                }

                this.sch.scheduleBegin = today.getTime();
                this.sch.scheduleEnd = today.getTime();
            }
        } else{
            this.arg = new Bundle();
            this.today = new Date(System.currentTimeMillis());
            this.sch.scheduleBegin = today.getTime();
            this.sch.scheduleEnd = today.getTime();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_schedule, container, false);
        String str;

        TextView textView = (TextView) returnView.findViewById(R.id.scheduleDate);
        textView.setText(dateFormat.format(today));

        EditText nameView = (EditText) returnView.findViewById(R.id.scheduleName);
        if(this.sch.name != null){
            nameView.setText(this.sch.name);
        }
        EditText memoView = (EditText) returnView.findViewById(R.id.memoText);
        if(this.sch.memo != null){
            memoView.setText(this.sch.memo);
        }
        TextView timeView = (TextView) returnView.findViewById(R.id.timeDetail);
        str = this.dateFormat.format(this.sch.scheduleBegin)
            + " ~ "
            + this.dateFormat.format(this.sch.scheduleEnd);
        timeView.setText(str);

        TextView locationView = (TextView) returnView.findViewById(R.id.locationDetail);
        if(this.sch.locationAddress != null){
            locationView.setText(this.sch.locationAddress);
        }

        TextView repeatView = (TextView) returnView.findViewById(R.id.repeationDetail);
        if(this.sch.isRepeated){
            switch(this.sch.repeatType){
                case ScheduleDataManager.RepeatDuration.REPEAT_DAYLY:
                    repeatView.setText(R.string.repeat_type_daily);
                    break;
                case ScheduleDataManager.RepeatDuration.REPEAT_WEEKLY:
                    StringBuilder sb = new StringBuilder(this.getContext().getString(R.string.repeat_type_weekly));
                    boolean cond = false;
                    if(this.sch.repeatDayOfWeek[0]) {
                        sb.append(' ');
                        for (int i = 1; i < 8; i++) {
                            if (this.sch.repeatDayOfWeek[i]) {
                                cond = true;
                                switch(i){
                                    case 1:
                                        sb.append(this.getContext().getString(R.string.sun));
                                        break;
                                    case 2:
                                        sb.append(this.getContext().getString(R.string.mon));
                                        break;
                                    case 3:
                                        sb.append(this.getContext().getString(R.string.tue));
                                        break;
                                    case 4:
                                        sb.append(this.getContext().getString(R.string.wed));
                                        break;
                                    case 5:
                                        sb.append(this.getContext().getString(R.string.thu));
                                        break;
                                    case 6:
                                        sb.append(this.getContext().getString(R.string.fri));
                                        break;
                                    case 7:
                                        sb.append(this.getContext().getString(R.string.sat));
                                        break;
                                }
                            }
                        }
                        repeatView.setText(sb.toString());
                    }
                    break;
                case ScheduleDataManager.RepeatDuration.REPEAT_MONTHLY:
                    repeatView.setText(R.string.repeat_type_monthly);
                    break;
                case ScheduleDataManager.RepeatDuration.REPEAT_YEARLY:
                    repeatView.setText(R.string.repeat_type_yearly);
                    break;
                default:
                    //something wrong
            }
        }else {
            repeatView.setText(R.string.repeat_type_none);
        }


        TextView alarmView = (TextView) returnView.findViewById(R.id.alarmDetail);
        if(this.sch.isRepeated){
            str = ScheduleDataManager.AlarmInterval.getTimeText(this.sch.alarmInterval);
            alarmView.setText(str);
        }

        TextView colorView = (TextView) returnView.findViewById(R.id.colorDetail);
        str = ScheduleDataManager.TableColor.getColorText(this.sch.tableColor);
        colorView.setText(str);

        ImageButton timeButton = (ImageButton) returnView.findViewById(R.id.timeButton);
        timeButton.setOnClickListener(this);

        ImageButton alarmButton = (ImageButton) returnView.findViewById(R.id.alarmButton);
        alarmButton.setOnClickListener(this);

        ImageButton colorButton = (ImageButton) returnView.findViewById(R.id.colorButton);
        colorButton.setOnClickListener(this);

        ImageButton repeatButton = (ImageButton) returnView.findViewById(R.id.repeationButton);
        repeatButton.setOnClickListener(this);

        ImageButton locationButton = (ImageButton) returnView.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(this);

        Button applyButton = (Button) returnView.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(this);

        Button deleteButton = (Button) returnView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        return returnView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(this.getClass().getName(), "onActivityResult, reqCode : " + requestCode + " resCode : " + resultCode);
        TextView tv;
        String str;
        if(requestCode == REQUEST_RESULT) {
            if(resultCode == RESULT_OK && data != null) {
                if(data.hasExtra(ScheduleRepeationActivity.EXTRA_NAME_ARGUMENT)){
                    Bundle bd = data.getBundleExtra(ScheduleRepeationActivity.EXTRA_NAME_ARGUMENT);
                    int rptCode = bd.getInt(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE);
                    Date rptEnd = new Date(bd.getLong(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_END));
                    this.sch.isRepeated = true;
                    this.sch.repeatType = rptCode;
                    this.sch.repeatEnd = rptEnd.getTime();
                    if(this.sch.repeatType == ScheduleDataManager.RepeatDuration.REPEAT_WEEKLY){
                        boolean[] rptDoW = bd.getBooleanArray(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_WEEKLY);
                        this.sch.repeatDayOfWeek[0] = true;
                        for(int i = 0; i < rptDoW.length; i++){
                            this.sch.repeatDayOfWeek[i + 1] = rptDoW[i];
                        }
                    }
                    Log.d(this.getClass().getName(), "repeatCode : " + rptCode + ", repeatEnd : " + rptEnd.toString());

                    tv = (TextView) getView().findViewById(R.id.repeationDetail);
                    if(bd.containsKey(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT)){
                        str = bd.getString(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT);
                        tv.setText(str);
                    }
                }

                if(data.hasExtra(ScheduleColorActivity.EXTRA_NAME_ARGUMENT)){
                    int crCode = data.getIntExtra(ScheduleColorActivity.EXTRA_NAME_ARGUMENT, ScheduleDataManager.TableColor.WHITE);
                    if(crCode > 0){
                        this.sch.tableColor = crCode;
                        str = ScheduleDataManager.TableColor.getColorText(crCode);
                        Log.d(this.getClass().getName(), "color : " + str);
                        tv = (TextView) getView().findViewById(R.id.colorDetail);
                        tv.setText(str);
                    }
                }

                if(data.hasExtra(ScheduleAlarmIntervalActivity.EXTRA_NAME_ARGUMENT)){
                    int almintCode = data.getIntExtra(ScheduleAlarmIntervalActivity.EXTRA_NAME_ARGUMENT, -1);
                    if(almintCode >= 0){
                        this.sch.hasAlarm = true;
                        this.sch.alarmInterval = almintCode;
                        str = ScheduleDataManager.AlarmInterval.getTimeText(almintCode);
                        Log.d(this.getClass().getName(), "alarm interval : " + almintCode);
                        tv = (TextView) getView().findViewById(R.id.alarmDetail);
                        tv.setText(str);
                    }
                }

                if(data.hasExtra(ScheduleTimeActivity.EXTRA_NAME_ARGUMENT)){
                    Bundle bd = data.getBundleExtra(ScheduleTimeActivity.EXTRA_NAME_ARGUMENT);
                    Date begin = new Date(bd.getLong(ScheduleTimeActivity.KEY_TIME_BEGIN));
                    Date end = new Date(bd.getLong(ScheduleTimeActivity.KEY_TIME_END));
                    this.sch.scheduleBegin = begin.getTime();
                    this.sch.scheduleEnd = end.getTime();
                    Log.d(this.getClass().getName(), "schedule begin : " + begin.toString() + ", end : " + end.toString());
                    str = dateFormat.format(begin) + " ~ " + dateFormat.format(end);
                    tv = (TextView) getView().findViewById(R.id.timeDetail);
                    tv.setText(str);
                }

                if(data.hasExtra(TMapActivity.EXTRA_NAME_ARGUMENT)){
                    Bundle bd = data.getBundleExtra(TMapActivity.EXTRA_NAME_ARGUMENT);
                    Double lat = bd.getDouble(MapFragment.KEY_LATITUDE);
                    Double lon = bd.getDouble(MapFragment.KEY_LONGITUDE);
                    String adr = bd.getString(MapFragment.KEY_ADDRESS);
                    this.sch.hasLocation = true;
                    this.sch.latitude = lat;
                    this.sch.longitude = lon;
                    this.sch.locationAddress = adr;
                    Log.d(this.getClass().getName(), "schedule location : " + "\naddress : " + adr + ", latitude : " + lat + ", longitude : " + lon);

                    tv = (TextView) getView().findViewById(R.id.locationDetail);
                    tv.setText(adr);
                }
//                Toast.makeText(getContext(), "Activity Terminated", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = null;
        FragmentTransaction ft = null;
        Intent intent;
        Bundle bd;
        switch(v.getId()){
            case R.id.timeButton:
                intent = new Intent(getActivity(), ScheduleTimeActivity.class);
                intent.putExtra(ScheduleTimeActivity.EXTRA_NAME_ARGUMENT, this.today.getTime());
                startActivityForResult(intent, REQUEST_RESULT);
                break;
            case R.id.alarmButton:
                intent = new Intent(getActivity(), ScheduleAlarmIntervalActivity.class);
                if(this.sch != null && this.sch.hasAlarm){
                    intent.putExtra(ScheduleAlarmIntervalActivity.EXTRA_NAME_ARGUMENT, this.sch.alarmInterval);
                }
                startActivityForResult(intent, REQUEST_RESULT);
                break;
            case R.id.colorButton:
                intent = new Intent(getActivity(), ScheduleColorActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
                break;
            case R.id.repeationButton:
                intent = new Intent(getActivity(), ScheduleRepeationActivity.class);
                if(this.sch != null && this.sch.isRepeated){
                    bd = new Bundle();
                    bd.putInt(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_CODE, this.sch.repeatType);
                    if(this.sch.repeatType == ScheduleDataManager.RepeatDuration.REPEAT_WEEKLY){
                        bd.putBooleanArray(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_WEEKLY, this.sch.repeatDayOfWeek);
                    }
                    bd.putLong(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_END, this.sch.repeatEnd);
                    try {
                        String repStr = ((TextView)this.getView().findViewById(R.id.repeationDetail)).getText().toString();
                        bd.putString(ScheduleRepeationActivity.ARGUMENT_KEY_REPEAT_TEXT, repStr);
                    }catch(Exception e){
                        Log.d(this.getClass().getName(), e.toString());
                    }
                    intent.putExtra(ScheduleRepeationActivity.EXTRA_NAME_ARGUMENT, bd);
                }
                startActivityForResult(intent, REQUEST_RESULT);
                break;
            case R.id.locationButton:
                intent = new Intent(getActivity(), TMapActivity.class);
                bd = new Bundle();
                if(this.sch != null && this.sch.hasLocation) {
                    bd.putDouble(MapFragment.KEY_LONGITUDE, this.sch.longitude);
                    bd.putDouble(MapFragment.KEY_LATITUDE, this.sch.latitude);
                    intent.putExtra(TMapActivity.EXTRA_NAME_ARGUMENT, bd);
                }else {
                    LocationDataManager.LocationData loca = LocationFileManager.getCurrentLocation(this.getContext());
                    if(loca != null){
                        bd.putDouble(MapFragment.KEY_LATITUDE, loca.latitude);
                        bd.putDouble(MapFragment.KEY_LONGITUDE, loca.longitude);
                        intent.putExtra(TMapActivity.EXTRA_NAME_ARGUMENT, bd);
                    }
                }
                startActivityForResult(intent, REQUEST_RESULT);
                break;
            case R.id.applyButton:
//                ImageView dotImg = (ImageView) getActivity().findViewById(R.id.onedayDotImg);
//
//                dotImg.getBackground().setColorFilter(Color.parseColor("#fe97a4"), PorterDuff.Mode.SRC_OVER);

                fragmentManager = this.getActivity().getSupportFragmentManager();
                EditText nameView = this.getView().findViewById(R.id.scheduleName);
                this.sch.name = nameView.getText().toString();;
                EditText memoView = this.getView().findViewById(R.id.memoText);
                this.sch.memo = memoView.getText().toString();
                this.arg.putSerializable(ScheduleFragment.ARG_KEY_SCHEDULE, this.sch);
                Log.d(this.getClass().getName(), "result schedule json : \n" + this.sch.toString());
                if(this.sch.name == null || this.sch.name.length() == 0){
                    Toast.makeText(this.getActivity(), "please put schedule name", Toast.LENGTH_SHORT).show();
                }else if(this.mListener != null){
                    ft =  fragmentManager.beginTransaction().hide(this);
                    this.mListener.onFragmentInteraction(this.arg);
                }

                break;
            case R.id.deleteButton:
                fragmentManager = this.getActivity().getSupportFragmentManager();

                this.arg.putBoolean(ScheduleFragment.ARG_KEY_DELETE, true);
                if(this.sch.name != null) {
                    this.arg.putString(ScheduleFragment.ARG_KEY_NAME, this.sch.name);
                    ft =  fragmentManager.beginTransaction().hide(this);
                }
                if(this.mListener != null){
                    this.mListener.onFragmentInteraction(this.arg);
                }
                break;
        }

        if(fragmentManager != null && ft != null){
            ft.commit();
            fragmentManager.popBackStack();
        }


    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle arg);
    }

    private void refresh() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

}
