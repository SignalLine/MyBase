package com.rilin.lzy.mybase.downmanager;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.rilin.lzy.mybase.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Created by rilintech on 16/10/13.
 */
public class DownLoadService extends Service {

    /**
     * 目标文件存储的文件夹路径
     */
    private String  destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "M_DEFAULT_DIR";
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "shan_yao.apk";

    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Retrofit.Builder retrofit;

    //下载的baseUrl
    private String baseUrl = "http://112.124.9.133:8080/parking-app-admin-1.0/android/manager/adminVersion/";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 在onStartCommand中进下载
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        //开始下载
        loadFile(baseUrl);
        return super.onStartCommand(intent, flags, startId);
    }

    //下载文件
    private void loadFile(String baseUrl){
        initNotification("0%","xxx软件");
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        retrofit.baseUrl(baseUrl)
                .client(initOkHttpClient())
                .build()
                .create(IFileLoad.class)
                .loadFile()
                .enqueue(new FileCallback(destFileDir,destFileName) {
                    @Override
                    public void onSuccess(File file) {
                        Log.e("download-->>", "-----------------请求成功");
                        // 安装软件
                        cancelNotification();
                        installApk(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        Log.e("download-->>", progress + "-------------" + total);
                        updateNotification(progress * 100 / total);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("download-->>", "----------------请求失败");
                        cancelNotification();
                    }
                });
    }

    public interface IFileLoad{
        @GET("download")
        Call<ResponseBody> loadFile();
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file){
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri,"application/vnd.android.package-archive");
        //执行意图进行安装
        mContext.startActivity(install);
    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

    /**
     * 初始化 Notification通知
     *
     * @param contentText
     * @param contentTitle
     */
    public void initNotification(String contentText,String contentTitle){
        builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 更新通知
     * @param progress
     */
    public void updateNotification(long progress){
        int currProgress = (int)progress;
        if(preProgress < currProgress){
            builder.setContentText(progress + "%");
            builder.setProgress(100,(int)progress,false);
            notificationManager.notify(NOTIFY_ID,builder.build());
        }
        preProgress = (int)progress;
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }
}
