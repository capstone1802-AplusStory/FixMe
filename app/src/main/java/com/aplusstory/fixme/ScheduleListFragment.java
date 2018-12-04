package com.aplusstory.fixme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleListFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_KEY_SCHEDULE = "argument_schedule";
    public static final String ARG_KEY_SCHEDULE_LIST = "argument_schedule_list";
    public static final String ARG_KEY_ADD = "argument_add";
    public static final String ARG_KEY_DELETE = "argument_delete";
    public static final String ARG_KEY_TODAY = "argument_today";
    public static final String ARG_KEY_SCHEDULE_LOAD = "argument_load";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FragmentManager fragmentManager = getFragmentManager();

    private Date today = new Date();
    SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
    private Bundle arg;
    private ArrayList<ScheduleDataManager.ScheduleData> schList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public ScheduleListFragment() {
        // Required empty public constructor
    }

    public static ScheduleListFragment newInstance(Bundle bd) {
        ScheduleListFragment fragment = new ScheduleListFragment();
        Bundle args = new Bundle(bd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arg = this.getArguments();
        if (this.arg != null) {
            if(this.arg.containsKey(ARG_KEY_TODAY)){
                this.today = new Date(this.arg.getLong(ARG_KEY_TODAY));
            }

            if(this.arg.containsKey(ARG_KEY_SCHEDULE_LIST)){
                Serializable argData = this.arg.getSerializable(ARG_KEY_SCHEDULE_LIST);
                ArrayList<?> argList = (ArrayList<?>)argData;
                for(Object arg : argList){
                    if(arg instanceof ScheduleDataManager.ScheduleData){
                        this.schList.add((ScheduleDataManager.ScheduleData)arg);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        TextView textView = (TextView) returnView.findViewById(R.id.today_date);
        //  TODO: setText to selected date
        textView.setText(date.format(today));

        recyclerView = (RecyclerView) returnView.findViewById(R.id.schedule_recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ScheduleListInfo> scheduleArrayList = new ArrayList<>();
        if(this.schList != null && this.schList.size() > 0) {
            for(ScheduleDataManager.ScheduleData sch : this.schList){
                scheduleArrayList.add(new ScheduleListInfo(ScheduleListInfo.getColorIconCode(sch.tableColor), sch.name, sch));

            }
        } else if(this.schList == null) {
            scheduleArrayList.add(new ScheduleListInfo(R.drawable.ic_red_circle_padding, "scheduleName", null));
        }
        ScheduleListColorRecyclerAdapter scheduleListColorRecyclerAdapter = new ScheduleListColorRecyclerAdapter(scheduleArrayList);
        recyclerView.setAdapter(scheduleListColorRecyclerAdapter);

        ImageButton addScheduleButton = (ImageButton) returnView.findViewById(R.id.add_schedule_button);
        addScheduleButton.setOnClickListener(this);


        return returnView;
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
        if(v.getId() == R.id.add_schedule_button){
            Bundle bd = new Bundle();
            bd.putBoolean(ARG_KEY_ADD, true);
            bd.putLong(ARG_KEY_TODAY,this.today.getTime());
            Log.d(this.getClass().getName(), "on add button click, data : " + this.today.toString());
            if(this.mListener != null){
                this.mListener.onFragmentInteraction(bd);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Bundle bd);
    }
}
