package com.aplusstory.fixme;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.Set;

public class FavoriteDataManager {
    public static final String FILENAME_FAVORITE = "favorite";

    private Context context;
    private SharedPreferences sp = null;
    private Set<String> favoList = null;

    public FavoriteDataManager(Context context){
        this.context = context;
        this.sp = this.context.getSharedPreferences(FILENAME_FAVORITE, 0);
        this.favoList = this.sp.getAll().keySet();
    }

    public boolean setFavoriteLocation(String name, LocationDataManager.LocationData loca){
        boolean rt = false;
        try {
            this.sp.edit().putString(name, loca.toString()).commit();
            this.refresh();
            rt = true;
        } catch(Exception e){
            Log.d(this.getClass().getName(), e.toString());
        }

        return rt;
    }

    public boolean rename(String before, String after){
        boolean rt = false;
        try {
            if(this.favoList.contains(before)){
                String data = this.sp.getString(before, null);
                if(data != null) {
                    SharedPreferences.Editor ed = this.sp.edit();
                    ed.putString(after, data).remove(before).commit();
                    this.refresh();
                    rt = true;
                }
            }
        } catch(Exception e){
            Log.d(this.getClass().getName(), e.toString());
        }

        return rt;
    }

    public LocationDataManager.LocationData getFavoriteLocation(String name){
        try {
            return LocationDataManager.LocationData.parseJSON(new JSONObject(this.sp.getString(name, null)));
        }catch (Exception e){
            Log.d(this.getClass().getName(), e.toString());
            return null;
        }
    }

    public Set<String> getFavoriteList(){
        return this.favoList;
    }

    public void refresh(){
        this.favoList = this.sp.getAll().keySet();
    }
}
