package com.aplusstory.fixme;

public interface ScheduleDataManager extends UserDataManager{
    @Override
    int setFileManager(FileManager f);
    int setLocationManger(LocationManager l);
}
