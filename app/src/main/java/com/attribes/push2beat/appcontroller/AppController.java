package com.attribes.push2beat.appcontroller;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by android on 2/6/17.
 */

public class AppController extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
