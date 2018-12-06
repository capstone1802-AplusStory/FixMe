package com.aplusstory.fixme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SwitchListviewAdapter extends ArrayAdapter<String> implements SettingsUIManager{
    private ArrayList<String> items;
    private Context context;
    private int id;
    private UserSettingsManager dm;

    public SwitchListviewAdapter(Context context, int resourceId, ArrayList<String> objects) {
        super(context, resourceId, objects);
        this.context = context;
        this.id = resourceId;
        this.items = objects;
        this.dm = new UserSettingsManager(this.context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        SwitchListviewAdapter that = SwitchListviewAdapter.this;


        if(v == null) {
            LayoutInflater viewInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = viewInflater.inflate(R.layout.switch_list, null);
        }

        TextView textView = (TextView) v.findViewById(R.id.switchText);
        textView.setText(items.get(position));

        Switch switchButton = (Switch)v.findViewById(R.id.switch1);

        switch(position){
            case 0:
                switchButton.setChecked(that.dm.getVibation());
                break;
            case 1:
                SharedPreferences sp = context.getSharedPreferences(NotificationUIService.NOTIFICATION_SERVICE_RUNNING_FLAG_SP_NAME,0);
                boolean isRunning = sp.contains(NotificationUIService.NOTIFICATION_SERVICE_RUNNING_FLAG_SP_KEY)
                    && sp.getBoolean(NotificationUIService.NOTIFICATION_SERVICE_RUNNING_FLAG_SP_KEY, false);
                switchButton.setChecked(isRunning);
                break;
            default:
                //something wrong
        }

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchListviewAdapter that = SwitchListviewAdapter.this;
                switch(position){
                    case 0:
                        that.dm.setViberation(isChecked);
                        break;
                    case 1:
                        Intent it = new Intent(that.context.getApplicationContext(), NotificationUIService.class);
                        if(isChecked){
                            it.setAction(NotificationUIService.NOTIFICATION_SERVICE_ACTION_START);
                        } else {
                            it.setAction(NotificationUIService.NOTIFICATION_SERVICE_ACTION_STOP);
                        }
                        that.context.startService(it);
                        break;
                }
            }
        });

        return v;
    }

    public ArrayList<String> getItems() {
        return this.items;
    }

    @Override
    public void setDataManager(UserDataManager m) {
        if(m instanceof SettingsDataManager){
            this.setDataManager((SettingsDataManager)m);
        }
    }

    @Override
    public void setDataManager(SettingsDataManager m) {
        if(m instanceof UserSettingsManager){
            this.dm = (UserSettingsManager)m;
        }
    }
}
