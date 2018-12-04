package com.aplusstory.fixme;

import android.content.Context;
import android.content.Intent;
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
    public static final String ARG_KEY_DELETE = "argument_delete";
    public static final String ARG_KEY_TODAY = "argument_today";

    private Bundle arg = null;
    Context context;

    Date today = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
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
            } else if(this.arg.containsKey(ARG_KEY_TODAY)){
                this.today = new Date(this.arg.getLong(ARG_KEY_TODAY));
            }
        } else{
            this.arg = new Bundle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_schedule, container, false);

        TextView textView = (TextView) returnView.findViewById(R.id.scheduleDate);
        textView.setText(date.format(today));

        EditText nameView = (EditText) returnView.findViewById(R.id.scheduleName);
        if(this.sch.name != null){
            nameView.setText(this.sch.name);
        }
        EditText memoView = (EditText) returnView.findViewById(R.id.memoText);
        if(this.sch.memo != null){
            memoView.setText(this.sch.memo);
        }

        TextView repeationDetail = (TextView) returnView.findViewById(R.id.repeationDetail);
        repeationDetail.setText("aaa");

        ImageButton timeButton = (ImageButton) returnView.findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleTimeActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });

        ImageButton alarmButton = (ImageButton) returnView.findViewById(R.id.alarmButton);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleAlarmIntervalActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });

        ImageButton colorButton = (ImageButton) returnView.findViewById(R.id.colorButton);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleColorActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });

        ImageButton repeatButton = (ImageButton) returnView.findViewById(R.id.repeationButton);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleRepeationActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });

        ImageButton locationButton = (ImageButton) returnView.findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ScheduleFragment.this.getContext(), "Map to select location", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TMapActivity.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });

        Button applyButton = (Button) returnView.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(this);

        Button deleteButton = (Button) returnView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        return returnView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(this.getClass().getName(), "onActivityResult, reqCode : " + requestCode + " resCode : " + resultCode);
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
                        for(int i = 0; i < rptDoW.length; i++){
                            this.sch.repeatDayOfWeek[i + 1] = rptDoW[i];
                        }
                    }
                    Log.d(this.getClass().getName(), "repeatCode : " + rptCode + ", repeatEnd : " + rptEnd.toString());
                }

                if(data.hasExtra(ScheduleColorActivity.EXTRA_NAME_ARGUMENT)){
                    int crCode = data.getIntExtra(ScheduleColorActivity.EXTRA_NAME_ARGUMENT, ScheduleDataManager.TableColor.WHITE);
                    if(crCode > 0){
                        this.sch.tableColor = crCode;
                        Log.d(this.getClass().getName(), "color : " + ScheduleDataManager.TableColor.getColorText(crCode));
                    }
                }

                if(data.hasExtra(ScheduleAlarmIntervalActivity.EXTRA_NAME_ARGUMENT)){
                    int almintCode = data.getIntExtra(ScheduleAlarmIntervalActivity.EXTRA_NAME_ARGUMENT, -1);
                    if(almintCode >= 0){
                        this.sch.hasAlarm = true;
                        this.sch.alarmInterval = almintCode;
                        Log.d(this.getClass().getName(), "alarm interval : " + almintCode);
                    }
                }

                if(data.hasExtra(ScheduleTimeActivity.EXTRA_NAME_ARGUMENT)){
                    Bundle bd = data.getBundleExtra(ScheduleTimeActivity.EXTRA_NAME_ARGUMENT);
                    Date begin = new Date(bd.getLong(ScheduleTimeActivity.KEY_TIME_BEGIN));
                    Date end = new Date(bd.getLong(ScheduleTimeActivity.KEY_TIME_END));
                    this.sch.scheduleBegin = begin.getTime();
                    this.sch.scheduleEnd = end.getTime();
                    Log.d(this.getClass().getName(), "schedule begin : " + begin.toString() + ", end : " + end.toString());
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
        switch(v.getId()){
            case R.id.applyButton:
//                ImageView dotImg = (ImageView) getActivity().findViewById(R.id.onedayDotImg);
//
//                dotImg.getBackground().setColorFilter(Color.parseColor("#fe97a4"), PorterDuff.Mode.SRC_OVER);

                fragmentManager = this.getActivity().getSupportFragmentManager();
                ft =  fragmentManager.beginTransaction().hide(this);
                EditText nameView = this.getView().findViewById(R.id.scheduleName);
                this.sch.name = nameView.getText().toString();;
                EditText memoView = this.getView().findViewById(R.id.memoText);
                this.sch.memo = memoView.getText().toString();
                this.arg.putSerializable(ScheduleFragment.ARG_KEY_SCHEDULE, this.sch);
                Log.d(this.getClass().getName(), "result schedule json : \n" + this.sch.toString());
                if(this.mListener != null){
                    this.mListener.onFragmentInteraction(this.arg);
                }

                break;
            case R.id.deleteButton:
                fragmentManager = this.getActivity().getSupportFragmentManager();
                ft =  fragmentManager.beginTransaction().hide(this);
                this.arg.putBoolean(ScheduleFragment.ARG_KEY_DELETE, true);
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
