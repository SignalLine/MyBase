package com.rilin.lzy.mybase.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.rilin.lzy.mybase.activity.BaseActivity;

/**
 * Created by rilintech on 16/9/30.
 */
public class BackGestureListener implements GestureDetector.OnGestureListener {

    private BaseActivity mBaseActivity;

    public BackGestureListener(BaseActivity baseActivity){
        mBaseActivity = baseActivity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if ((e2.getX() - e1.getX()) >100 && Math.abs(e1.getY() - e2.getY()) < 60 ) {
            mBaseActivity.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
