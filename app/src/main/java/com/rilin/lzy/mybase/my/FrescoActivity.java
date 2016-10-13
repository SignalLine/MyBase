package com.rilin.lzy.mybase.my;


import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;

/**
 * fresco图片加载 需要添加依赖
 * compile 'com.facebook.fresco:fresco:0.13.0'
 * 在MyApplication中初始化
 * Fresco.initialize(this);
 */
public class FrescoActivity extends BaseActivity {

    private SimpleDraweeView mDraweeView,mDraweeView2,mDraweeView3,mDraweeView4,
                    mDraweeView5,mDraweeView6,mDraweeView7,mDraweeView8,mDraweeView9;

    private String url = "http://www.easyicon.net/api/resizeApi.php?id=507335&size=128";
    @Override
    public int setViewId() {
        return R.layout.activity_fresco;
    }

    @Override
    public void initView() {
        mDraweeView = getView(R.id.fresco_sdv_1);
        mDraweeView2 = getView(R.id.fresco_sdv_2);
        mDraweeView3 = getView(R.id.fresco_sdv_3);

        mDraweeView4 = getView(R.id.fresco_sdv_4);
        mDraweeView5 = getView(R.id.fresco_sdv_5);
        mDraweeView6 = getView(R.id.fresco_sdv_6);
        mDraweeView7 = getView(R.id.fresco_sdv_7);
        mDraweeView8 = getView(R.id.fresco_sdv_8);
        mDraweeView9 = getView(R.id.fresco_sdv_9);
    }

    @Override
    public void initData() {
        //创建要下载的图片的uri
        Uri imageUri1 = Uri.parse(url);
        mDraweeView.setImageURI(imageUri1);
        //开始下载
        mDraweeView2.setImageURI(imageUri1);

        //错误下载地址
        mDraweeView3.setImageURI(imageUri1 + "1");
        //--重试功能--会重复加载4次,如果还没有加载出来才会显示failure图片----------------
        mDraweeView4.setImageURI(imageUri1 + "1");
        //创建DraweeController
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageUri1 + "1")
                .setTapToRetryEnabled(true)//设置点击重试是否开启
                .setOldController(mDraweeView4.getController())//设置旧的Controller
                .build();
        //设置DraweeController
        mDraweeView4.setController(controller);

        //
        mDraweeView5.setImageURI(imageUri1);
        //创建DraweeController
        DraweeController controller5 = Fresco.newDraweeControllerBuilder()
                .setUri(imageUri1)
                .setTapToRetryEnabled(true)//设置点击重试是否开启
                .setOldController(mDraweeView5.getController())//设置旧的Controller
                .build();
        //设置DraweeController
        mDraweeView5.setController(controller5);

        mDraweeView6.setImageURI(imageUri1);
        //创建DraweeController
        DraweeController controller6 = Fresco.newDraweeControllerBuilder()
                .setUri(imageUri1)
                .setTapToRetryEnabled(true)//设置点击重试是否开启
                .setOldController(mDraweeView6.getController())//设置旧的Controller
                .build();
        //设置DraweeController
        mDraweeView6.setController(controller6);

        mDraweeView7.setImageURI(imageUri1);
        //创建DraweeController
        DraweeController controller7 = Fresco.newDraweeControllerBuilder()
                .setUri(imageUri1)
                .setTapToRetryEnabled(true)//设置点击重试是否开启
                .setOldController(mDraweeView7.getController())//设置旧的Controller
                .build();
        //设置DraweeController
        mDraweeView7.setController(controller7);

        mDraweeView8.setImageURI(imageUri1);
        //创建DraweeController
        DraweeController controller8 = Fresco.newDraweeControllerBuilder()
                .setUri(imageUri1)
                .setTapToRetryEnabled(true)//设置点击重试是否开启
                .setOldController(mDraweeView8.getController())//设置旧的Controller
                .build();
        //设置DraweeController
        mDraweeView8.setController(controller8);

        mDraweeView9.setImageURI(imageUri1);
        //创建DraweeController
        DraweeController controller9 = Fresco.newDraweeControllerBuilder()
                .setUri(imageUri1)
                .setTapToRetryEnabled(true)//设置点击重试是否开启
                .setOldController(mDraweeView9.getController())//设置旧的Controller
                .build();
        //设置DraweeController
        mDraweeView9.setController(controller9);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void destroyResource() {

    }
}
