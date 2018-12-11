package com.aplusstory.fixme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChartRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public static class ColorRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView percentText, locationText, timeText;

        public ColorRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            percentText = itemView.findViewById(R.id.percent_text);
            locationText = itemView.findViewById(R.id.location_text);
            timeText = itemView.findViewById(R.id.time_text);
        }
    }

    private ArrayList<ChartInfo> chartInfoArrayList;
    ChartRecyclerAdapter(ArrayList<ChartInfo> chartInfoArrayList) {
        this.chartInfoArrayList = chartInfoArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_chart_row, viewGroup, false);
        context = viewGroup.getContext();
        return new ColorRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ColorRecyclerViewHolder colorRecyclerViewHolder = (ColorRecyclerViewHolder) viewHolder;
        ChartInfo info = this.chartInfoArrayList.get(i);
        String percentText = info.percentData + "%";
        String locationText;
        if(info.locationName.length() > 10){
            StringBuilder sb = new StringBuilder(info.locationName);
            locationText = sb.substring(0, 10) + "...";
        } else {
            locationText = info.locationName;
        }

        String timeText = info.timeData;

        colorRecyclerViewHolder.percentText.setText(percentText);
        colorRecyclerViewHolder.locationText.setText(locationText);
        colorRecyclerViewHolder.timeText.setText(timeText);

        GradientDrawable textViewBackGround = (GradientDrawable)colorRecyclerViewHolder.percentText.getBackground();

        switch(i % 11) {
            case 0:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor1));
                break;
            case 1:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor2));
                break;
            case 2:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor3));
                break;
            case 3:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor4));
                break;
            case 4:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor5));
                break;
            case 5:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor6));
                break;
            case 6:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor7));
                break;
            case 7:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor8));
                break;
            case 8:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor9));
                break;
            case 9:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor10));
                break;
            case 10:
                textViewBackGround.setColor(context.getResources().getColor(R.color.chartColor11));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chartInfoArrayList.size();
    }
}
