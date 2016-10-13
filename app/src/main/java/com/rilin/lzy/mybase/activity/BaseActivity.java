package com.rilin.lzy.mybase.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.utils.JUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.dialog.LoadingDialog;
import com.rilin.lzy.mybase.it.IT4BaseActivity;
import com.rilin.lzy.mybase.net.NetTaskUtils;



/**
 * Project:BaseProject
 * Author:zhouya
 */
public abstract class BaseActivity extends AppCompatActivity implements IT4BaseActivity {
    private int screenHeight;
    private int screenWidth;
    private LoadingDialog loadingDialogFragment;

    public AppCompatActivity mActivity;
    public Context mContext;

    //系统状态栏设置
    public SystemBarTintManager tintManager;

//    /**
//     * 手势监听
//     */
//    GestureDetector mGestureDetector;
//    /**
//     * 是否需要监听手势关闭功能
//     */
//    private boolean mNeedBackGesture = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = getApplicationContext();

        //
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenHeight = outMetrics.heightPixels;
        screenWidth = outMetrics.widthPixels;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initSystemBar(this);
//        initGestureDetector();

        JUtils.initialize(getApplication());

        //初始化notification服务
        initService();

        setContentView(setViewId());
        initView();
        initData();
    }

//    //手势
//    private void initGestureDetector() {
//        if (mGestureDetector == null) {
//            mGestureDetector = new GestureDetector(getApplicationContext(),
//                    new BackGestureListener(BaseActivity.this));
//        }
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mNeedBackGesture) {
//            return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    /*
     * 设置是否进行手势监听
     */
//    public void setNeedBackGesture(boolean mNeedBackGesture) {
//        this.mNeedBackGesture = mNeedBackGesture;
//    }

    /*
     * 返回
     */
    public void doBack(View view) {
        onBackPressed();
    }

    /**
     * 设置状态栏颜色
     * @param activity
     */
    public void initSystemBar(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintColor(Color.parseColor("#3dc164"));
    }

    public void showLoadingDialog() {
        if (loadingDialogFragment == null) {
            loadingDialogFragment = LoadingDialog.createDialog(this);
        }
        if (!loadingDialogFragment.isShowing() && loadingDialogFragment != null) {
            loadingDialogFragment.show();
        }
    }

    public void dismissLoadDialog() {
        if (loadingDialogFragment != null && loadingDialogFragment.isShowing()) {
            loadingDialogFragment.dismiss();
        }
    }

    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {

            return (E) findViewById(id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }
    }

    /**
     * 得到屏幕高度
     *
     * @return
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyResource();
        if (loadingDialogFragment != null && loadingDialogFragment.isShowing())
            loadingDialogFragment.dismiss();
        loadingDialogFragment = null;

    }

    protected NetTaskUtils getRequstClient() {
        return NetTaskUtils.init();
    }


    protected final void finishActivityByAniamtion() {
        this.finish();
        overridePendingTransition(R.anim.a_my_out_zoom1, R.anim.a_my_out_zoom2);
    }


    protected final void startActivityByAniamtion(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.a_my_in_scale_min_max1,
                R.anim.a_my_in_alpha_action2);
    }


    protected final void startActivityByAniamtion(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(R.anim.a_my_in_scale_min_max1,
                R.anim.a_my_in_alpha_action2);
    }

    protected final void startActivityForResult(Class<?> c, int requestCode) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, requestCode);
    }


    protected final void startActivityForResultByAniamtion(Intent intent,
                                                           int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.a_my_in_scale_min_max1,
                R.anim.a_my_in_alpha_action2);
    }

    /**
     * @param c
     */
    protected final void startActivity(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    protected final void toast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    protected final void showCustomToast(String msg) {
        Toast toast = new Toast(this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.custom_toast_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_tv);
        tv.setText(msg);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //JPushInterface.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //JPushInterface.onPause(this);
    }

    //notifycation-----------------------
    public NotificationManager mNotificationManager;

    private void initService(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public void clearNotify(int notifyId){
        mNotificationManager.cancel(notifyId);
    }

    public void clearAllNotify(){
        mNotificationManager.cancelAll();
    }

    public PendingIntent getDefaultIntent(int flags){
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,new Intent(),flags);
        return pendingIntent;
    }

}
