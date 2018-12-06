package com.aplusstory.fixme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;


public class PieChartFragment extends Fragment implements View.OnClickListener{
    public static final String KEY_DATE = "today";
    public static final String KEY_TYPE = "chart_type";
    public static final int TYPE_TIME = 0;
    public static final int TYPE_RATIO = 1;

    private Bundle arg = null;
    private int sortType = 0;
    private OnFragmentInteractionListener mListener;
    PieChart pieChart;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    public PieChartFragment() {
    }

    public static PieChartFragment newInstance(Bundle arg) {
        PieChartFragment fragment = new PieChartFragment();
        fragment.setArguments(arg);
        return fragment;
    }
    GestureDetector gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener() {@Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }
    });
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arg = this.getArguments();
        if(this.arg != null) {
            this.sortType = this.arg.getInt(KEY_TYPE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        Date today;
        if(this.arg != null && this.arg.containsKey(KEY_DATE)){
            today = new Date(this.arg.getLong(KEY_DATE));
        }else {
            today = new Date();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        TextView chartDate = (TextView) returnView.findViewById(R.id.chart_date);
        chartDate.setText(dateFormat.format(today));

        pieChart = (PieChart) returnView.findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setEntryLabelColor(Color.BLACK);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        ArrayList<?> argList = null;
        FootprintDataManager.FootPrintData ftd = null;


        if(this.arg != null && this.arg.containsKey(FootprintDataManager.KEY_DATA)){
            Serializable arg = this.arg.getSerializable(FootprintDataManager.KEY_DATA);
            if(arg instanceof ArrayList<?>){
                argList = (ArrayList<?>)((ArrayList) arg).clone();
                if(this.sortType == TYPE_RATIO) {
                    argList.sort(new Comparator<Object>() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            if (o1 instanceof FootprintDataManager.FootPrintData && o2 instanceof FootprintDataManager.FootPrintData) {
                                return Double.compare(
                                        ((FootprintDataManager.FootPrintData) o1).getInterval(),
                                        ((FootprintDataManager.FootPrintData) o2).getInterval()
                                );
                            } else if (o1 instanceof FootprintDataManager.FootPrintData) {
                                return 1;
                            } else if (o2 instanceof FootprintDataManager.FootPrintData) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });
                }
                int cnt = 0;
                for(Object obj : argList){
                    if(obj instanceof FootprintDataManager.FootPrintData){
                        ftd = (FootprintDataManager.FootPrintData)obj;
                        Log.d(this.getClass().getName(), "footprint data : " + ftd.toString());
//                        if(this.sortType == TYPE_TIME) {
                            String s = ftd.name;
                            if (s == null) {
                                s = "location " + cnt++;
                            }
                            yValues.add(new PieEntry(ftd.getInterval(), s));
//                        }else if(this.sortType == TYPE_RATIO){
//
//                        }
                    }
                }
            }
        } else {
            //test data
            yValues.add(new PieEntry(31, "집"));
            yValues.add(new PieEntry(10, "이동"));
            yValues.add(new PieEntry(25, "학교"));
            yValues.add(new PieEntry(4, "이동"));
            yValues.add(new PieEntry(6, "스타시티 건대"));
            yValues.add(new PieEntry(8, "아즈텍 PC방"));
            yValues.add(new PieEntry(4, "이동"));
            yValues.add(new PieEntry(12, "집"));
        }
//        if(this.sortType == TYPE_RATIO){
//            yValues.sort(new Comparator<PieEntry>() {
//                @Override
//                public int compare(PieEntry o1, PieEntry o2) {
//                    if(o1.getValue() > o2.getValue()){
//                        return 1;
//                    } else if(o1.getValue() == o2.getValue()){
//                        return 0;
//                    }else /*if(o1.getValue() < o2.getValue())*/{
//                        return -1;
//                    }
//                }
//            });
//        }
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues, "Locations");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        if(this.arg != null && this.arg.containsKey(FootprintDataManager.KEY_DATA)){
            ArrayList<Integer> colorArr = new ArrayList<>();
            for(int i = 0; i < yValues.size(); i++){
                switch(i % 11){
                    case 0:
                        colorArr.add(this.getResources().getColor(R.color.chartColor1));
                        break;
                    case 1:
                        colorArr.add(this.getResources().getColor(R.color.chartColor2));
                        break;
                    case 2:
                        colorArr.add(this.getResources().getColor(R.color.chartColor3));
                        break;
                    case 3:
                        colorArr.add(this.getResources().getColor(R.color.chartColor4));
                        break;
                    case 4:
                        colorArr.add(this.getResources().getColor(R.color.chartColor5));
                        break;
                    case 5:
                        colorArr.add(this.getResources().getColor(R.color.chartColor6));
                        break;
                    case 6:
                        colorArr.add(this.getResources().getColor(R.color.chartColor7));
                        break;
                    case 7:
                        colorArr.add(this.getResources().getColor(R.color.chartColor8));
                        break;
                    case 8:
                        colorArr.add(this.getResources().getColor(R.color.chartColor9));
                        break;
                    case 9:
                        colorArr.add(this.getResources().getColor(R.color.chartColor10));
                        break;
                    case 10:
                        colorArr.add(this.getResources().getColor(R.color.chartColor11));
                        break;
                    default:
                        colorArr.add(this.getResources().getColor(R.color.transparent));
                }
                dataSet.setColors(colorArr);
            }
        } else {
            //test data color
            dataSet.setColors(new int[]{getResources().getColor(R.color.chartColor1), getResources().getColor(R.color.chartColor2)
                    , getResources().getColor(R.color.chartColor3), getResources().getColor(R.color.chartColor4)
                    , getResources().getColor(R.color.chartColor5), getResources().getColor(R.color.chartColor6)
                    , getResources().getColor(R.color.chartColor7), getResources().getColor(R.color.chartColor8)
                    , getResources().getColor(R.color.chartColor9), getResources().getColor(R.color.chartColor10)
                    , getResources().getColor(R.color.chartColor11)});
        }
        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new PercentFormatter());

        pieChart.setData(data);

        recyclerView = (RecyclerView) returnView.findViewById(R.id.chart_recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<ChartInfo> chartInfoArrayList = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("HH:mm");

        if(this.arg != null && this.arg.containsKey(FootprintDataManager.KEY_DATA) && argList != null){
            for(int i = 0; i < dataSet.getEntryCount(); i++){
                ftd = (FootprintDataManager.FootPrintData)argList.get(i);
                String timeDateStr = "";
                if(this.sortType == TYPE_TIME){
                    ftd = (FootprintDataManager.FootPrintData)argList.get(i);
                    timeDateStr = df.format(new Date(ftd.dtBigin)) + "~" + df.format(new Date(ftd.dtEnd));
                }else if(this.sortType == TYPE_RATIO){
                    StringBuilder sb = new StringBuilder();
//                    long interval = (long)dataSet.getEntryForIndex(i).getValue();
                    long interval = ftd.getInterval();
                    Log.d(this.getClass().getName(), "interval : " + interval);
                    if(interval / (60 * 60000) > 0){
                        sb.append(interval / (60 * 60000))
                        .append(this.getContext().getString(R.string.hour_period))
                        .append((interval % (60 * 60000)) / 60000)
                        .append(this.getContext().getString(R.string.minute));
                    }else if(interval / 60000 > 0){
                        sb.append(interval / 60000)
                        .append(this.getContext().getString(R.string.minute));
                    }else {
                        sb.append(this.getContext().getString(R.string.less_then_a_minute));
                    }
                    timeDateStr = sb.toString();
                }
                chartInfoArrayList.add(
                        new ChartInfo(
                                String.valueOf(dataSet.getEntryForIndex(i).getValue() / data.getYValueSum() * 100.0F)
                                , dataSet.getEntryForIndex(i).getLabel()
                                , timeDateStr
                                , ftd
                        )
                );
            }
        } else {
            //test data
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(0).getValue()), dataSet.getEntryForIndex(0).getLabel(), "00:00~08:00"));
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(1).getValue()), dataSet.getEntryForIndex(1).getLabel(), "08:00~10:30"));
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(2).getValue()), dataSet.getEntryForIndex(2).getLabel(), "10:30~16:30"));
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(3).getValue()), dataSet.getEntryForIndex(3).getLabel(), "16:30~17:30"));
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(4).getValue()), dataSet.getEntryForIndex(4).getLabel(), "17:30~18:00"));
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(5).getValue()), dataSet.getEntryForIndex(5).getLabel(), "18:00~19:00"));
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(6).getValue()), dataSet.getEntryForIndex(6).getLabel(), "19:00~20:30"));
            chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(7).getValue()), dataSet.getEntryForIndex(7).getLabel(), "20:30~21:00"));
        }
        ChartRecyclerAdapter chartRecyclerAdapter = new ChartRecyclerAdapter(chartInfoArrayList);
        recyclerView.setAdapter(chartRecyclerAdapter);

        Button toggleButton = (Button) returnView.findViewById(R.id.ChartTypeToggle);
        if(this.sortType == TYPE_TIME){
            toggleButton.setText(R.string.button_footprint_to_ratiochart);
        } else if(this.sortType == TYPE_RATIO){
            toggleButton.setText(R.string.button_footprint_to_timechart);
        }
        toggleButton.setOnClickListener(this);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                //손으로 터치한 곳의 좌표를 토대로 해당 Item의 View를 가져옴
                View childView = rv.findChildViewUnder(e.getX(),e.getY());
                if(childView != null && gestureDetector.onTouchEvent(e)){
                    int currentPosition = rv.getChildAdapterPosition(childView);

                    ChartInfo currentChartInfo = chartInfoArrayList.get(currentPosition);

                    if(currentChartInfo.footPrintData.locaDataType == LocationDataManager.PathData.class) {
                        Intent intent = new Intent(getActivity(), FootprintRoutineActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("MovementData",currentChartInfo.footPrintData.locaData);
                        intent.putExtra("MovementData", currentChartInfo.footPrintData.locaData);
//                        FootprintRoutineActivity footprintRoutineActivity = new FootprintRoutineActivity();
//                        footprintRoutineActivity.startActivity(intent);
                        PieChartFragment.this.getActivity().startActivity(intent);
                    }

                    return true;
                }
                return false;

            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
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
        switch(v.getId()){
            case R.id.ChartTypeToggle:
                FragmentManager fm = this.getActivity().getSupportFragmentManager();
                if(fm != null){
                    Fragment fg = new PieChartFragment();
                    Bundle bd;
                    if(this.arg != null) {
                        bd = new Bundle(this.arg);
                    }else {
                        bd = new Bundle();
                        bd.putLong(KEY_DATE, System.currentTimeMillis());
                    }
                    if(this.sortType == TYPE_TIME) {
                        bd.putInt(KEY_TYPE, TYPE_RATIO);
                    }else if(this.sortType == TYPE_RATIO){
                        bd.putInt(KEY_TYPE, TYPE_TIME);
                    }
                    fg.setArguments(bd);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.footprint_frame, fg);
                    ft.addToBackStack(ft.getClass().getSimpleName());
                    fg.getFragmentManager().popBackStack(ft.getClass().getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.commit();
                }
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
}
