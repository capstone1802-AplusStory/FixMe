package com.aplusstory.fixme;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleListColorRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context = null;
    private ScheduleListFragment.OnFragmentInteractionListener listener = null;

    public static class ScheduleColorRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView scheduleImageView;
        TextView scheduleTextView;

        public ScheduleColorRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleImageView = itemView.findViewById(R.id.colorImage);
            scheduleTextView = itemView.findViewById(R.id.colorText);
        }
    }

    private ArrayList<ScheduleListInfo> scheduleListInfoArrayList;

    ScheduleListColorRecyclerAdapter(ArrayList<ScheduleListInfo> scheduleListInfoArrayList) {
        this.scheduleListInfoArrayList = scheduleListInfoArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_row, viewGroup, false);
        this.context = viewGroup.getContext();
        if(context instanceof ScheduleListFragment.OnFragmentInteractionListener) {
            this.listener = (ScheduleListFragment.OnFragmentInteractionListener)context;
        }

        return new ScheduleColorRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ScheduleColorRecyclerViewHolder scheduleColorRecyclerViewHolder = (ScheduleColorRecyclerViewHolder) viewHolder;
        ScheduleListInfo info = scheduleListInfoArrayList.get(i);
        if(info.scheduleColorID > 0) {
            scheduleColorRecyclerViewHolder.scheduleImageView.setImageResource(info.scheduleColorID);
        }
        scheduleColorRecyclerViewHolder.scheduleTextView.setText(info.scheduleName);
        scheduleColorRecyclerViewHolder.scheduleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleListColorRecyclerAdapter that = ScheduleListColorRecyclerAdapter.this;
                ScheduleDataManager.ScheduleData sch = that.scheduleListInfoArrayList.get(i).scheduleData;
                if(sch != null){
                    Log.d(that.getClass().getName(), "schedule edit, data : " + sch.toString());
                    Bundle bd = new Bundle();
                    bd.putSerializable(ScheduleFragment.ARG_KEY_EDIT, sch);
                    if(that.listener != null) {
                        that.listener.onFragmentInteraction(bd);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleListInfoArrayList.size();
    }
}
