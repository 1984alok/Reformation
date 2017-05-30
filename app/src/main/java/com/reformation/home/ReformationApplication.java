package com.reformation.home;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import java.util.ArrayList;

import model.EvenTCatg;
import services.LocationFetchingService;
import utils.FontUtls;

/**
 * Created by IMFCORP\alok.acharya on 11/4/17.
 */
public class ReformationApplication extends MultiDexApplication {

    public ArrayList<EvenTCatg> getEventCat() {
        return eventCat;
    }

    public void setEventCat(ArrayList<EvenTCatg> eventCat) {
        this.eventCat = eventCat;
    }

    ArrayList<EvenTCatg> eventCat;

    @Override
    public void onCreate() {
        super.onCreate();
        FontUtls.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(new Intent(this, LocationFetchingService.class));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


}
