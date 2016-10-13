package com.rilin.lzy.mybase.downmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by rilintech on 16/10/13.
 */
public class UpdateManager {
    private Context mContext;
    public UpdateManager(Context context){
        mContext = context;
    }

    public void checkUpdate(String updateInfo,final boolean isToast){
        //这里请求后台接口,获取更新的内容和最新的版本号
//        String version_info = "更新内容\n" + "    1. 车位分享异常处理\n" + "    2. 发布车位折扣格式统一\n" + "    ";
        int mVersion_code = DeviceUtils.getVersionCode(mContext);
        int nVersion_code = 2;
        if(mVersion_code < nVersion_code){
            //显示提示对话框
            showNoticeDialog(updateInfo);
        }else {
            if(isToast){
                Toast.makeText(mContext,"已经是最新版本",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNoticeDialog(String updateInfo){
        //构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("更新提示")
        .setMessage(updateInfo)
        .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mContext.startService(new Intent(mContext,DownLoadService.class));
            }
        })
        .setNegativeButton("以后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //显示
        builder.create().show();
    }
}
