package com.aplusstory.fixme;

import android.content.Context;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aplusstory.fixme.cal.OneDayView;

import java.time.Month;

import static android.support.constraint.Constraints.TAG;


public class CalendarFragment extends Fragment {
    public static final String ARG_PARAM_CALENDER = "calendar";
    FragmentManager monthlyFragmentManager;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;
    private TextView thisMonthTv = null;
    private Bundle arg;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param year Parameter 1.
     * @param month Parameter 2.
     * @return A new instance of fragment YearlyCalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(int year, int month) {
        CalendarFragment fragment = new CalendarFragment();
        MonthlyFragment mf = MonthlyFragment.newInstance(year,month);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, year);
        args.putInt(ARG_PARAM2, month);
        fragment.setArguments(args);
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
        monthlyFragmentManager = this.getChildFragmentManager();
        MonthlyFragment mf = new MonthlyFragment();
        if(getArguments()!=null && getArguments().containsKey(ARG_PARAM1) && getArguments().containsKey(ARG_PARAM2)){
            mf = MonthlyFragment.newInstance(getArguments().getInt(ARG_PARAM1),getArguments().getInt(ARG_PARAM2));
        }
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
                } else if(context instanceof FootprintActivity){
                    ((FootprintActivity) CalendarFragment.this.getContext()).onDayClick(dayView);
                }
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.main_container,mf).commit();


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
