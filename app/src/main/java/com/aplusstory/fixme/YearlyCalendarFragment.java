package com.aplusstory.fixme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ik024.calendar_lib.custom.YearView;

import java.util.Calendar;

import io.github.memfis19.cadar.view.MonthCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YearlyCalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YearlyCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YearlyCalendarFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    YearView yearView;
    MonthCalendar monthCalendar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public YearlyCalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YearlyCalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YearlyCalendarFragment newInstance(String param1, String param2) {
        YearlyCalendarFragment fragment = new YearlyCalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_yearly_calendar, container, false);

        TextView janTextView = (TextView) returnView.findViewById(R.id.janText),
                febTextView = (TextView) returnView.findViewById(R.id.febText),
                marTextView = (TextView) returnView.findViewById(R.id.marText),
                aprTextView = (TextView) returnView.findViewById(R.id.aprText),
                mayTextView = (TextView) returnView.findViewById(R.id.mayText),
                junTextView = (TextView) returnView.findViewById(R.id.junText),
                julTextView = (TextView) returnView.findViewById(R.id.julText),
                augTextView = (TextView) returnView.findViewById(R.id.augText),
                sepTextView = (TextView) returnView.findViewById(R.id.sepText),
                octTextView = (TextView) returnView.findViewById(R.id.octText),
                novTextView = (TextView) returnView.findViewById(R.id.novText),
                decTextView = (TextView) returnView.findViewById(R.id.decText);

        //set background of textview
        janTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        febTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        marTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        aprTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        mayTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        junTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        julTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        augTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        sepTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        octTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        novTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        decTextView.setBackgroundColor(getContext().getResources().getColor(R.color.graybg20));
        janTextView.setOnClickListener(this);
        febTextView.setOnClickListener(this);
        marTextView.setOnClickListener(this);
        aprTextView.setOnClickListener(this);
        mayTextView.setOnClickListener(this);
        junTextView.setOnClickListener(this);
        julTextView.setOnClickListener(this);
        augTextView.setOnClickListener(this);
        sepTextView.setOnClickListener(this);
        octTextView.setOnClickListener(this);
        novTextView.setOnClickListener(this);
        decTextView.setOnClickListener(this);

        return returnView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    @Override
    public void onClick(View v) {
        CalendarFragment calendarFragment = (CalendarFragment)new CalendarFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Calendar today = Calendar.getInstance();
        int now_year = today.get(Calendar.YEAR);
        int now_month = today.get(Calendar.MONTH);
        int real_year = now_year;

        switch(v.getId()){
            case R.id.janText:
//                if(now_month < 0) real_year--;
                ft.replace(R.id.footprint_frame,calendarFragment.newInstance(real_year,0));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.febText:
                if(now_month - 1 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,1));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.marText:
                if(now_month - 2 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,2));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.aprText:
                if(now_month - 3 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,3));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.mayText:
                if(now_month - 4 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,4));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.junText:
                if(now_month - 5 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,5));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.julText:
                if(now_month - 6 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,6));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.augText:
                if(now_month - 7 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,7));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.sepText:
                if(now_month - 8 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,8));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.octText:
                if(now_month - 9 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,9));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.novText:
                if(now_month - 10 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,10));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.decText:
                if(now_month - 11 < 0) real_year--;
                ft.replace(R.id.footprint_frame,CalendarFragment.newInstance(real_year,11));
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//
//        menu.findItem(R.id.ic_footprint_calendar).setVisible(false);
//    }


}
