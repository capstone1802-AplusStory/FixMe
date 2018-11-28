package com.aplusstory.fixme;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aplusstory.fixme.cal.OneDayView;


public class CalendarFragment extends Fragment {
    public static final String ARG_PARAM_CALENDER = "calendar";

    private OnFragmentInteractionListener mListener;
    private TextView thisMonthTv = null;
    private Bundle arg;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getClass().getName(), "Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        thisMonthTv = (TextView)v.findViewById(R.id.this_month_tv);

        MonthlyFragment mf = (MonthlyFragment) getChildFragmentManager().findFragmentById(R.id.monthly_calender);
        mf.setOnMonthChangeListener(new MonthlyFragment.OnMonthChangeListener() {
            @Override
            public void onChange(int year, int month) {
                Log.d(CalendarFragment.this.getClass().getName(), "onChange " + year + "." + month);
                if(CalendarFragment.this.thisMonthTv != null) {
                    CalendarFragment.this.thisMonthTv.setText(year + "." + (month + 1));
                }
                Context context = CalendarFragment.this.getContext();
                if(context instanceof ScheduleActivity) {
                    ((ScheduleActivity) CalendarFragment.this.getContext()).onChange(year, month);
                }
            }

            @Override
            public void onDayClick(OneDayView dayView) {
                Context context = CalendarFragment.this.getContext();
                if(context instanceof ScheduleActivity) {
                    ((ScheduleActivity) CalendarFragment.this.getContext()).onDayClick(dayView);
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle arg);
    }
}
