package com.aplusstory.fixme;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Date;


public class PieChartFragment extends Fragment {
    public static final String KEY_DATE = "today";

    private Bundle arg = null;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arg = this.getArguments();
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

        if(this.arg != null && this.arg.containsKey(FootprintDataManager.KEY_DATA)){
            Serializable arg = this.arg.getSerializable(FootprintDataManager.KEY_DATA);
            if(arg instanceof ArrayList<?>){
                argList = (ArrayList<?>)arg;
                int cnt = 0;
                for(Object obj : argList){
                    if(obj instanceof FootprintDataManager.FootPrintData){
                        FootprintDataManager.FootPrintData ftd = (FootprintDataManager.FootPrintData)obj;
                        Log.d(this.getClass().getName(), "footprint data : " + ftd.toString());
                        String s = ftd.name;
                        if(s == null){
                            s = "location " + cnt++;
                        }
                        yValues.add(new PieEntry(ftd.getInterval(), s));
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

        ArrayList<ChartInfo> chartInfoArrayList = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("HH:mm");

        if(this.arg != null && this.arg.containsKey(FootprintDataManager.KEY_DATA) && argList != null){
            for(int i = 0; i < dataSet.getEntryCount(); i++){
                FootprintDataManager.FootPrintData ftd = (FootprintDataManager.FootPrintData)argList.get(i);
                chartInfoArrayList.add(
                        new ChartInfo(
                                String.valueOf(dataSet.getEntryForIndex(i).getValue())
                                , dataSet.getEntryForIndex(i).getLabel()
                                , df.format(new Date(ftd.dtBigin)) + "~" + df.format(new Date(ftd.dtEnd))
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
