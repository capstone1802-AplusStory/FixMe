package com.aplusstory.fixme;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PieChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    PieChart pieChart;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    public PieChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PieChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PieChartFragment newInstance(String param1, String param2) {
        PieChartFragment fragment = new PieChartFragment();
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
        View returnView = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        Date today = new Date();
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

        //add chart data
        yValues.add(new PieEntry(31, "집"));
        yValues.add(new PieEntry(10, "이동"));
        yValues.add(new PieEntry(25, "학교"));
        yValues.add(new PieEntry(4, "이동"));
        yValues.add(new PieEntry(6, "스타시티 건대"));
        yValues.add(new PieEntry(8, "아즈텍 PC방"));
        yValues.add(new PieEntry(4, "이동"));
        yValues.add(new PieEntry(12, "집"));


        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues, "Locations");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(new int[] {getResources().getColor(R.color.chartColor1), getResources().getColor(R.color.chartColor2)
                , getResources().getColor(R.color.chartColor3), getResources().getColor(R.color.chartColor4)
                , getResources().getColor(R.color.chartColor5), getResources().getColor(R.color.chartColor6)
                , getResources().getColor(R.color.chartColor7), getResources().getColor(R.color.chartColor8)
                , getResources().getColor(R.color.chartColor9), getResources().getColor(R.color.chartColor10)
                , getResources().getColor(R.color.chartColor11)});

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
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(0).getValue()), dataSet.getEntryForIndex(0).getLabel(), "00:00~08:00"));
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(1).getValue()), dataSet.getEntryForIndex(1).getLabel(), "08:00~10:30"));
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(2).getValue()), dataSet.getEntryForIndex(2).getLabel(), "10:30~16:30"));
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(3).getValue()), dataSet.getEntryForIndex(3).getLabel(), "16:30~17:30"));
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(4).getValue()), dataSet.getEntryForIndex(4).getLabel(), "17:30~18:00"));
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(5).getValue()), dataSet.getEntryForIndex(5).getLabel(), "18:00~19:00"));
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(6).getValue()), dataSet.getEntryForIndex(6).getLabel(), "19:00~20:30"));
        chartInfoArrayList.add(new ChartInfo(String.valueOf(dataSet.getEntryForIndex(7).getValue()), dataSet.getEntryForIndex(7).getLabel(), "20:30~21:00"));

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
