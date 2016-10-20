package com.rilin.lzy.mybase.application;

import android.app.Application;
import android.os.Environment;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.rilin.lzy.mybase.model.Engine;
import com.rilin.lzy.mybase.model.ProvinceView;
import com.rilin.lzy.mybase.util.FileUtils;
import com.rilin.lzy.mybase.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by lzy on 2016/1/8.
 */
public class MyApplication extends Application {

    public static boolean isDebug;

    public String IP;

    //private static String IPNUMBER = "http://192.168.0.122:3002/";

    private static final String IPNUMBER = "http://121.40.195.149:3093/";

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getIP() {
        return IP;
    }

    //播放视频
    public static final String OPLAYER_CACHE_BASE = Environment.getExternalStorageDirectory() + "/oplayer";
    public static final String OPLAYER_VIDEO_THUMB = OPLAYER_CACHE_BASE + "/thumb/";
    public static final String PREF_KEY_FIRST = "application_first";
    private static MyApplication mApplication;

//    Retrofit
    private Engine mEngine;


    public static List<ProvinceView> provinceList = new ArrayList<ProvinceView>();
    public static List<ProvinceView> provinceList_index = new ArrayList<ProvinceView>();
    public static String provinceid = "";
    public static String cityid = "";
    public static String countyid = "";
    public static String area_province_name = "";
    public static String area_city_name = "";
    public static String area_county_name = "";

    @Override
    public void onCreate() {
        super.onCreate();

        isDebug = true;
        mApplication = this;
//        SugarContext.init(getApplicationContext());
//
//        //创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
//                .writeDebugLogs() //打印log信息
//                .build();
//
//
//        //Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);

        setIP(IPNUMBER);
        //第二个参数是appkey，就是百川应用创建时候的appkey
//        FeedbackAPI.initAnnoy(this, "23343188");

        //先初始化
        Fresco.initialize(this);

        //视频播放,存放路径初始化
        FileUtils.createIfNoExists(OPLAYER_CACHE_BASE);
        FileUtils.createIfNoExists(OPLAYER_VIDEO_THUMB);


        //Retrofit
        mEngine = new Retrofit.Builder()
                .baseUrl("http://7xk9dj.com1.z0.glb.clouddn.com/banner/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Engine.class);
    }


    public Engine getEngine() {
        return mEngine;
    }

    public static MyApplication getApplication(){
        return mApplication;
    }

    public void destroy(){
        mApplication = null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        SugarContext.terminate();
    }
}
