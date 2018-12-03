package com.aplusstory.fixme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

//TODO : implement
public class PathRecoder extends Service implements LocationDataManager {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void setFileManager(FileManager fm) {

    }
}
