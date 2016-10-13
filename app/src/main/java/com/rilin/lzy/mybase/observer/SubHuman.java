package com.rilin.lzy.mybase.observer;

import android.util.Log;

/**
 * Created by lzy on 16/9/22.
 */
public class SubHuman implements ISubScribe {
    private static final String TAG = "SuHuman";
    //订阅者名字
    private String username;

    public SubHuman(String username){
        this.username = username;
    }

    //告诉订阅者有新报纸了
    @Override
    public void HasNewPaper() {
        Log.i(TAG,"---" + username + "--->>有新的报纸了,请查收");
    }
}
