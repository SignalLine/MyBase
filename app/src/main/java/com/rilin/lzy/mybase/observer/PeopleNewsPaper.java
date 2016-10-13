package com.rilin.lzy.mybase.observer;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzy on 16/9/22.
 */
public class PeopleNewsPaper implements INewsPaper {

    private static final String TAG = "PeopleNewsPaper";
    private List<ISubScribe> subList = new ArrayList<>();

    @Override
    public void RegisterSubscriber(ISubScribe subScribe) {
        subList.add(subScribe);
        Log.i(TAG,"-----RegisterSubscriber-------");
    }

    @Override
    public void RemoveSubscriber(ISubScribe subScribe) {
        if(subList.indexOf(subScribe) >= 0){
            subList.remove(subScribe);
            Log.i(TAG,"-----RemoveSubscriber----------");
        }
    }

    //发送报纸
    @Override
    public void SendPaper() {
        Log.i(TAG,"-----sendPaper------------");
        for (ISubScribe s: subList) {
            s.HasNewPaper();
        }
    }
}
