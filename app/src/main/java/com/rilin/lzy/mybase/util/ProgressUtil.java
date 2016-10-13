package com.rilin.lzy.mybase.util;
import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

/**
 * Created by rilintech on 16/3/9.
 */
public class ProgressUtil {

    private static SVProgressHUD svProgressHUD;

    private ProgressUtil() {
    }

    private static ProgressUtil instance;

    public static ProgressUtil getInstance(){
        if(null == instance){
            instance = new ProgressUtil();

        }
        return instance;
    }

    public void showProgress(Context context, String progressString){
        svProgressHUD = new SVProgressHUD(context);
        svProgressHUD.showWithStatus(progressString);
    }
    public void showSuccessProgress(String progressString){
        svProgressHUD.showSuccessWithStatus(progressString);
        svProgressHUD.dismiss();
    }
    public void showErrorProgress(String progressString){
        svProgressHUD.showErrorWithStatus(progressString);
        svProgressHUD.dismiss();
    }
    public void showUnDoneProgress(Context context, String progressString){
        svProgressHUD = new SVProgressHUD(context);
        svProgressHUD.showInfoWithStatus(progressString);
        //svProgressHUD.dismiss();
    }
    public void showWithProgress(Context context, String progressString){
        svProgressHUD = new SVProgressHUD(context);
        svProgressHUD.showWithProgress(progressString, SVProgressHUD.SVProgressHUDMaskType.Black);
    }
    public void showWithProgressDetail( String progressString){

        svProgressHUD.showWithProgress(progressString, SVProgressHUD.SVProgressHUDMaskType.Black);

        if(Integer.parseInt(progressString) >= 100){
            svProgressHUD.dismiss();
        }

//        if(progressString.equals("100")){
//            svProgressHUD.dismiss();
//        }
    }

    public void showDoneProgress(){
        svProgressHUD.dismiss();
    }

}
