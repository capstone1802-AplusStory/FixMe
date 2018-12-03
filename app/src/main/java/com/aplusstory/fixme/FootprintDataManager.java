package com.aplusstory.fixme;

public interface FootprintDataManager extends UserDataManager{
    @Override
    void setFileManager(FileManager f);
    int setLocationDataManager(LocationDataManager l);
}
